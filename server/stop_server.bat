@echo off

echo Set environment variables defined in file env-vars.txt
for /F %%g in (env-vars.txt) do set PATH=%%g

echo Stop docker-compose
docker-compose down
