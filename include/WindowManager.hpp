#pragma once

#include <cstddef>
#include <X11/Xlib.h>
#include <GL/gl.h>
#include <GL/glx.h>
#include <utility>

#include "Color.hpp"
#include "TriangleDrawer.hpp"

namespace window_management {

class WindowManager {
private:
    // window properties
    std::size_t width;
    std::size_t height;
    Color color;

    // X11
    Display *display = NULL;
    Window window;
    XVisualInfo *visualInfo = NULL;
    int screenID = 0;

    // OpenGL
    GLint attributes[5] = { GLX_RGBA, GLX_DEPTH_SIZE, 24, GLX_DOUBLEBUFFER, None };
    GLXContext glxContext;

    TriangleDrawer triangleDrawer;
    bool shouldDrawTriangle = false;

    std::pair<std::size_t, std::size_t> getMousePosition();
    std::pair<std::size_t, std::size_t> getWindowSize();
    
    void clear();

public:
    WindowManager(std::size_t width, std::size_t height, Color color);
    ~WindowManager();

    WindowManager(const WindowManager &) = delete;
    WindowManager(WindowManager &&) = delete;
    WindowManager &operator=(const WindowManager &) = delete;
    WindowManager &operator=(WindowManager &&) = delete;

    void update();
};

} // namespace window_management