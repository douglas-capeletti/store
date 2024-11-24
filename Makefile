default: help
.PHONY: help production development database-stop database database-stop database-reset database-login

help: # Show help for each of the Makefile recipes.
	@grep -E '^[a-zA-Z0-9 -]+:.*#'  Makefile | while read -r l; do printf "\033[1;32m$$(echo $$l | cut -f 1 -d':')\033[00m:$$(echo $$l | cut -f 2- -d'#')\n";done

production: # Run the production Dockerfile
	docker compose up --build --detach --remove-orphans

development: # Run the development Dockerfile (including the app itself)
	docker compose -f docker-compose-dev.yaml up --build --remove-orphans # --detach

development-stop: # Stop the development Dockerfile
	docker compose -f docker-compose-dev.yaml down

database: # Run the development database
	make development-stop
	docker compose -f docker-compose-local.yaml up -d

database-stop: # Stop the development database
	docker compose -f docker-compose-local.yaml down

database-reset: # Remove the content from database and force the application to recreate it
	 docker volume rm store_db_data

database-login:
	docker exec -it database_dev psql -U store -d store
