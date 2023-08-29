
class Coordinates(var x: Double, var y: Double, var z: Double, var w: Double) {

    fun coordinates() {
        x = 0.0
        y = 0.0
        z = 0.0
        w = 1.0

    }

    fun coordinates(x: Double, y: Double, z: Double, w: Double) {
        this.x = x
        this.y = y
        this.z = z
        this.w = w
    }

    fun normalize() {
        if (w != 0.0) {
            x /= w
            y /= w
            z /= w
            w = 1.0
        } else {
            w = 1.0
        }
    }
}