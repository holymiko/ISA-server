cd "$(dirname "$0")"

sudo docker image remove -f isa-server-prod:latest   # resolves bug, forces rebuilding of image
sudo docker compose build
sudo docker compose --profile prod up -d --remove-orphans
