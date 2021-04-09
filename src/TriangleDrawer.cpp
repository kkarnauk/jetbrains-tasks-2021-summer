#include <cstddef>
#include <GL/gl.h>
#include <GL/glu.h>
#include "TriangleDrawer.hpp"

namespace window_management {

void TriangleDrawer::draw(double x, double y) {
    glMatrixMode(GL_PROJECTION);
    glLoadIdentity();
    glOrtho(-1.0, 1.0, -1.0, 1.0, 1.0, 20.0);

    glMatrixMode(GL_MODELVIEW);
    glLoadIdentity();
    gluLookAt(0.0, 0.0, 10.0, 0.0, 0.0, 0.0, 0.0, 1.0, 0.0);
    glBegin(GL_TRIANGLES);
    glColor3f(1.0, 0.0, 0.0); glVertex3f(x, y, 0.0);
    glColor3f(0.0, 1.0, 0.0); glVertex3f(x + 0.2, y + 0.2, 0.0);
    glColor3f(1.0, 1.0, 0.0); glVertex3f(x + 0.2, y, 0.0);
    glEnd();
}

} // namespace window_management