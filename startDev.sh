cd "$(dirname "$0")"

sudo docker container remove isa-server-dev-1
sudo docker image remove -f isa-server-dev:latest   # resolves bug, forces rebuilding of image
sudo docker compose build --no-cache
sudo docker compose --profile dev up --remove-orphans
