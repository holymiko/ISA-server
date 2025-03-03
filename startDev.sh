cd "$(dirname "$0")"

# resolves bug, forced rebuild
sudo docker container remove isa-client-dev-1
sudo docker container remove isa-server-dev-1
sudo docker image remove -f isa-client-dev:latest
sudo docker image remove -f isa-server-dev:latest

sudo docker compose build --no-cache
sudo docker compose --profile dev up --remove-orphans
