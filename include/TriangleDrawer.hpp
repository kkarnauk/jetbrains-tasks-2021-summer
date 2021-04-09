#pragma once

namespace window_management {

class TriangleDrawer {
private:

public:
    TriangleDrawer() = default;
    ~TriangleDrawer() = default;

    void draw(double x, double y);
};

} // namespace window_management