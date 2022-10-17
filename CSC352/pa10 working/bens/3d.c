/**
 * File: 3d.h
 * Author: Benjamin Dicken
 * Course: CS 352 at the UofA
 * Description:
 * A C library for creating 3D shapes and scenes.
 * This header includes several structs needed for representing the 3D shapes.
 * It also contains the prototypes for several of the public-facing functions.
 */

#include <stdio.h>
#include <stdint.h>
#include <assert.h>
#include <stdlib.h>
#include <string.h>
#include <math.h>
#include <stdbool.h>
#include "3d.h"

/**
 * A Helper function to append a Triangle3DNode to an Object3D.
 *   Parameters:
 *     object - the object to append to
 *     node   - the node to append
 */
void Object3D_append_object_node(Object3D* object, Triangle3DNode* node) {
  object->count += 1;
  if (object->root == NULL) {
    object->root = node;
    return;
  }
  Triangle3DNode* temp = object->root;
  while (temp->next != NULL) {
     temp = temp->next;
  }
  temp->next = node;
}

/**
 * A Helper function to append a Triangle3D to an Object3D.
 *   Parameters:
 *     object   - the object to append to
 *     triangle - the triangle to append
 */
void Object3D_append_triangle(Object3D* object, Triangle3D triangle) {
  Triangle3DNode* node = calloc(1, sizeof(Triangle3DNode));
  node->triangle = triangle;
  Object3D_append_object_node(object, node);
}

/**
 * Compute the distance between two points.
 *   Parameters:
 *     a / b - Coordinates in 3D space
 *   Return:
 *     The computed distance represented as a double
 */
double Coordinate3D_distance(Coordinate3D a, Coordinate3D b) {
  return sqrt( pow(a.x - b.x, 2.0) + pow(a.y - b.y, 2.0) + pow(a.z - b.z, 2.0));
}

/**
 * Determine the two closest points, and the farthest point, from a given point.
 *   Parameters:
 *     starting - The coordinate to begin distance measuring from
 *     coords   - An array of 3 coordinates 
 *     closest1 / closest2 - The two closest point to starting
 *     Farthest - The farthest point from starting
 */
void Coordinate3D_get_closest_two(
    Coordinate3D starting, Coordinate3D* coords, 
    Coordinate3D* closest1, Coordinate3D* closest2,
    Coordinate3D* farthest) {
  double zero = Coordinate3D_distance(starting, coords[0]);
  double one  = Coordinate3D_distance(starting, coords[1]);
  double two  = Coordinate3D_distance(starting, coords[2]);
  if (zero <= two && one <= two) {
    *closest1 = coords[0];
    *closest2 = coords[1];
    *farthest = coords[2];
  } else if (one <= zero && two <= zero) {
    *closest1 = coords[1];
    *closest2 = coords[2];
    *farthest = coords[0];
  } else if (two <= one && zero <= one){
    *closest1 = coords[0];
    *closest2 = coords[2];
    *farthest = coords[1];
  } else {
    assert(false);
  }
}

#define DOUBLE_MARGIN 0.0001

/**
 * Sort a list of Coordinate3Ds.
 *   Parameters:
 *     array - The array of coordinates to sort
 *     size - the number of elements in the array of coordinates
 */
void Coordinate3D_sort(Coordinate3D* array, int size) {
  for (int i = 0; i < size; i++) {
    for (int j = 0; j < size-1; j++) {
      Coordinate3D ca = array[j];
      Coordinate3D cb = array[j+1];
      if ( (ca.x - cb.x) >= DOUBLE_MARGIN ) {
        array[j] = cb; array[j+1] = ca;
      } else if ( (cb.x - ca.x) >= DOUBLE_MARGIN ) {
        continue;
      } else if ( (ca.y - cb.y) >= DOUBLE_MARGIN ) {
        array[j] = cb; array[j+1] = ca;
      } else if ( (cb.y - ca.y) >= DOUBLE_MARGIN ) {
        continue;
      } else if ( (ca.z - cb.z) >= DOUBLE_MARGIN ) {
        array[j] = cb; array[j+1] = ca;
      }
    }
  }
}

void Object3D_append_quadrilateral(Object3D* o, 
    Coordinate3D a, Coordinate3D b, Coordinate3D c, Coordinate3D d) {

  Coordinate3D starting, closest1, closest2, farthest;
  Coordinate3D co[] = {a, b, c, d};
  Coordinate3D_sort(co, 4);
  a = co[0]; b = co[1]; c = co[2]; d = co[3];
  double max_distance = 0.0;
  double distances[6] = {0.0, 0.0, 0.0, 0.0, 0.0, 0.0};
  int ci = 0;
  int maxi = 0;
  
  // Find the coordinate that is furthest from the other three
  // Store into "starting" and its index into the "distances" array into "ci"
  for (int i = 0; i < 3; i++) {
    for (int j = i+1; j < 4; j++) {
      distances[ci] = Coordinate3D_distance(co[i], co[j]);
      // Handle the case of two points being the "same"
      if (distances[ci] < DOUBLE_MARGIN) {
        Triangle3D single = (Triangle3D){b, c, d}; 
        if (i == 0){
          single = (Triangle3D) {b, c, d};
        } else if (i == 1) {
          single = (Triangle3D) {a, c, d};
        } else if (i == 2) {
          single = (Triangle3D) {a, b, d};
        }
        Object3D_append_triangle(o, single);
        return;
      }
      if (distances[ci] > (max_distance + DOUBLE_MARGIN)) {
        max_distance = distances[ci];
        starting = co[i];
        maxi = ci;
      }
      ci += 1;
    }
  }
 
  // Gather the points other than the starting one
  Coordinate3D tcoords[3] = {b, c, d};
  if (maxi == 3 || maxi == 4) {
    tcoords[0] = a; tcoords[1] = c; tcoords[2] = d;
  } else if (maxi == 5) {
    tcoords[0] = a; tcoords[1] = b; tcoords[2] = d;
  }

  // Put a triangle between starting point, and two closest to it
  Coordinate3D_get_closest_two(starting, tcoords, &closest1, &closest2, &farthest);
  Triangle3D t1 = (Triangle3D) {starting, closest1, closest2};
  Object3D_append_triangle(o, t1); 

  // For the remaining unused point, find two closest, and build second triangle
  tcoords[0] = closest1; tcoords[1] = closest2; tcoords[2] = starting;
  Coordinate3D_get_closest_two(farthest, tcoords, &closest1, &closest2, &starting);
  Triangle3D t2 = (Triangle3D) {farthest, closest1, closest2};
  Object3D_append_triangle(o, t2); 
}

