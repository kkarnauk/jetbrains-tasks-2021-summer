#include <stdexcept>
#include <iostream>
#include "WindowManager.hpp"

using namespace window_management;

int main() {
    try {
        WindowManager manager(400, 400, Color(37, 133, 75));
        while (true) {
            manager.update();
        }
    } catch (std::exception &e) {
        std::cout << e.what() << std::endl;
    }
}