#include <stdio.h>
#include <stdlib.h>

typedef struct {
    char *name;
    int size;
    void (*(*_vtable)[])();
} metadata;

// VEHICLE
/*
class Vehicle { // implicit extends Object
    void start() { }
    int getColor() { return 9; }
}
*/
typedef struct {
    metadata *clazz;
    void (*start)(); // set this to &Vehicle_start
} Vehicle;

void Vehicle_start(Vehicle *this) { printf("Vehicle_start\n"); }

// TRUCK

/*
class Truck extends Vehicle {
    void start() { }
    void setPayload(int n) { }
}
*/
typedef struct {
    metadata *clazz;
    void (*start)(); // set this to &Truck_start
} Truck;

void Truck_start(Vehicle *this) { printf("Truck_start\n"); }

int main(int argc, char *argv[])
{
	Vehicle *v = (Vehicle *)calloc(1, sizeof(Vehicle));
	v->start = &Vehicle_start;

	Truck *t = (Truck *)calloc(1, sizeof(Truck));
	t->start = &Truck_start;

	(*v->start)(v);
	(*t->start)(t);

	v = (Vehicle *)t;
	(*v->start)(v);
}
