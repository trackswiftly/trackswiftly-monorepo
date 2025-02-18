FROM ubuntu:plucky

RUN mkdir -p /main_project

COPY ./target/vehicle_service /main_project/


WORKDIR /main_project

ENTRYPOINT ["./vehicle_service"]