#include <stdexcept>
#include "WindowManager.hpp"

namespace window_management {

WindowManager::WindowManager(std::size_t width_, std::size_t height_, Color color_) :
    width{width_}, height{height_}, color{color_} {

    display = XOpenDisplay(NULL);
    if (display == NULL) {
        throw std::runtime_error("Cannot open window.");
    }

    Window root = DefaultRootWindow(display);
    visualInfo = glXChooseVisual(display, 0, attributes);
    if (visualInfo == NULL) {
        throw std::runtime_error("Cannot find appropriate visual.");
    }

    screenID = DefaultScreen(display);
    window = XCreateSimpleWindow(display, root, 0, 0, width, height, 0,
                                 BlackPixel(display, screenID), color.getPixel());
    

    XSelectInput(display, window, ButtonPressMask | ButtonReleaseMask | PointerMotionMask | StructureNotifyMask);
    XMapWindow(display, window);

    glxContext = glXCreateContext(display, visualInfo, NULL, GL_TRUE);
    glXMakeCurrent(display, window, glxContext);
}

WindowManager::~WindowManager() {
    glXMakeCurrent(display, None, NULL);
    glXDestroyContext(display, glxContext);
    XDestroyWindow(display, window);
    XCloseDisplay(display);
}

void WindowManager::update() {
    XEvent event;
    XNextEvent(display, &event);

    clear();
    if (event.type == ConfigureNotify) {
        width = event.xconfigure.width;
        height = event.xconfigure.height; 
    } else if (event.type == ButtonPress) {
        shouldDrawTriangle = true;
    } else if (event.type == ButtonRelease) {
        shouldDrawTriangle = false;
    }

    if (shouldDrawTriangle) {
        const auto &[x, y] = getMousePosition();
        triangleDrawer.draw(2.0 * x / width - 1, 1 - 2.0 * y / height);
        glXSwapBuffers(display, window);
    }
}

std::pair<std::size_t, std::size_t> WindowManager::getMousePosition() {
    int xPos;
    int yPos;
    int winXPos;
    int winYPos;
    unsigned int maskReturned;
    Window windowReturned;
    XQueryPointer(display, window, &windowReturned, &windowReturned, 
                  &xPos, &yPos, &winXPos, &winYPos, &maskReturned);

    return std::make_pair(winXPos, winYPos);
}

void WindowManager::clear() {
    const auto &[r, g, b] = color;
    glViewport(0, 0, width, height);
    glClearColor(r / 255.0, g / 255.0, b / 255.0, 1.0);
    glClear(GL_COLOR_BUFFER_BIT);
}

} // namespace window_management