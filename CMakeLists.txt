cmake_minimum_required(VERSION 3.11)

project(window_management)

set(CMAKE_CXX_COMPILER c++)
set(CMAKE_CXX_STANDARD 17)
set(CMAKE_CXX_FLAGS "${CMAKE_CXX_FLAGS} -Wall -Wextra -Werror")

include_directories("include/")

find_package(X11 REQUIRED)
include_directories(${X11_INCLUDE_DIR})
link_directories(${X11_LIBRARIES})

find_package(OpenGL REQUIRED)
include_directories(${OPENGL_INCLUDE_LIBRARY})

add_executable(window_management
    src/main.cpp
    src/Color.cpp
    src/WindowManager.cpp
    src/TriangleDrawer.cpp
)

target_link_libraries(window_management ${X11_LIBRARIES} ${OPENGL_LIBRARY})
