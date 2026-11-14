"""Run with python3 docker-compose/test_safe_defaults.py (Docker Compose required)."""
import json
import os
from pathlib import Path
import subprocess
import tempfile

ROOT = Path(__file__).resolve().parent
for kind, secret, bind in [("mysql", "MYSQL_ROOT_PASSWORD", "MYSQL_BIND_ADDRESS"),
                           ("redis", "REDIS_PASSWORD", "REDIS_BIND_ADDRESS")]:
    with tempfile.TemporaryDirectory() as tmp:
        compose = Path(tmp) / "compose.yml"
        compose.write_bytes((ROOT / kind / "docker-compose.yml").read_bytes())
        env = {k: v for k, v in os.environ.items()
               if not k.startswith(("MYSQL_", "REDIS_", "COMPOSE_"))}
        cmd = ["docker", "compose", "--project-name", "safe-defaults-test",
               "--env-file", os.devnull, "-f", str(compose), "config", "--format", "json"]
        missing = subprocess.run(cmd, env=env, capture_output=True, text=True)
        assert missing.returncode != 0 and secret in missing.stderr, missing.stderr
        for address in ["127.0.0.1", "192.0.2.10"]:
            configured = dict(env, **{secret: "test-only-placeholder"})
            if address != "127.0.0.1":
                configured[bind] = address
            result = subprocess.run(cmd, env=configured, capture_output=True, text=True)
            assert result.returncode == 0, result.stderr
            service = json.loads(result.stdout)["services"][kind]
            assert service["ports"][0]["host_ip"] == address
            if kind == "mysql":
                assert "MYSQL_ALLOW_EMPTY_PASSWORD" not in service["environment"]
                assert "MYSQL_ROOT_HOST" not in service["environment"]
            else:
                assert service["command"][-1] == "test-only-placeholder"
    print("PASS:", kind, "missing-secret rejection, loopback default, explicit bind")
