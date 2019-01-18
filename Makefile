local-dev-start:
	docker-compose -f docker-compose/aws/dynamodb.yml up -d --remove-orphans


local-dev-end:
	docker-compose -f docker-compose/aws/dynamodb.yml down -v --remove-orphans


# clean up all compiled cache in the project.
clean:
	rm -rf ./target
	rm -rf ./project/target
	rm -rf ./common/target
	rm -rf ./subproject1/target
	rm -rf ./subproject2/target

