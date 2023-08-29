package com.example.quaternationapp

import Coordinates
import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.view.View
import androidx.core.internal.view.SupportMenu
import java.util.Timer
import java.util.TimerTask


class MyQuaternionView(context: Context) : View(context) {
    private var redPaint //paint object for drawing the lines
            : Paint? = null


    private var cube_vertices: ArrayList<Coordinates> = ArrayList()
    private var draw_cube_vertices: ArrayList<Coordinates> = ArrayList()

    private var headVertices: ArrayList<Coordinates> =ArrayList()


    init {
        var thisview: MyQuaternionView = this
        val paint = Paint(1)
        redPaint = paint
        paint.style = Paint.Style.FILL
        paint.color = SupportMenu.CATEGORY_MASK
        paint.strokeWidth = 2.0f
        //create a 3D cube

        val cube_vertices: ArrayList<Coordinates> = ArrayList()
        cube_vertices.add(Coordinates(-1.0, -1.0, -1.0, 1.0))
        cube_vertices.add(Coordinates(-1.0, -1.0, 1.0, 1.0))
        cube_vertices.add(Coordinates(-1.0, 1.0, -1.0, 1.0))
        cube_vertices.add(Coordinates(-1.0, 1.0, 1.0, 1.0))
        cube_vertices.add(Coordinates(1.0, -1.0, -1.0, 1.0))
        cube_vertices.add(Coordinates(1.0, -1.0, 1.0, 1.0))
        cube_vertices.add(Coordinates(1.0, 1.0, -1.0, 1.0))
        cube_vertices.add(Coordinates(1.0, 1.0, 1.0, 1.0))


        val translate: ArrayList<Coordinates> = translate(cube_vertices, 6.0, 2.12, 5.0)
        headVertices = translate
        headVertices = scale(translate, 80.0, 80.0, 80.0)
        invalidate()

        Timer().scheduleAtFixedRate(object : TimerTask() {
            override fun run() {

                var dir = true
                if (-90 == -90 && 1 == 0) {
                    dir = true
                } else if (1 != 0 && -90 == 90) {
                    dir = false
                }
                if (dir) {
                    val i = -90 + 1
                    this@MyQuaternionView.rotateRobot(0.0, i.toDouble(), 1.0)
                } else {
                    val i2 = -90 - 1
                    this@MyQuaternionView.rotateRobot(0.0, i2.toDouble(), 1.0)
                }

                thisview.invalidate();

            }
        }, 100L, 100L)


        /**
         * Gimbal Lock example
         */


    }

    private fun DrawLinePairs(
        canvas: Canvas, vertices: ArrayList<Coordinates>, start: Int, end: Int, paint: Paint
    ) { //draw a line connecting 2 points
        //canvas - canvas of the view
        //points - array of points
        //start - index of the starting point
        //end - index of the ending point
        //paint - the paint of the line
        canvas.drawLine(
            (vertices.get(start).x)!!.toFloat(),
            (vertices.get(start).y)!!.toFloat(),
            (vertices[end].x)!!.toFloat(),
            (vertices[end].y)!!.toFloat(),
            paint
        )
    }

    fun rotateRobot(thetaX: Double, thetaY: Double, thetaZ: Double) {
        val QuaternionRotate: ArrayList<Coordinates> = QuaternionRotate(headVertices, thetaX, 0, 1, 0)
        headVertices = QuaternionRotate
        val QuaternionRotate2: ArrayList<Coordinates> =
            QuaternionRotate(QuaternionRotate, thetaY, 0, 1, 0)
        headVertices = QuaternionRotate2
        headVertices = QuaternionRotate(QuaternionRotate2, thetaZ, 0, 1, 0)

    }

    override fun onDraw(canvas: Canvas) {
        //draw objects on the screen
        super.onDraw(canvas)
        DrawCube(canvas, headVertices, redPaint!!) //draw a cube onto the screen



    }
    private fun DrawCube(
        canvas: Canvas,
        draw_cube_vertices: ArrayList<Coordinates>,
        c_paint: Paint
    ) {
        DrawLinePairs(canvas, draw_cube_vertices, 0, 1, c_paint)
        DrawLinePairs(canvas, draw_cube_vertices, 1, 3, c_paint)
        DrawLinePairs(canvas, draw_cube_vertices, 3, 2, c_paint)
        DrawLinePairs(canvas, draw_cube_vertices, 2, 0, c_paint)
        DrawLinePairs(canvas, draw_cube_vertices, 4, 5, c_paint)
        DrawLinePairs(canvas, draw_cube_vertices, 5, 7, c_paint)
        DrawLinePairs(canvas, draw_cube_vertices, 7, 6, c_paint)
        DrawLinePairs(canvas, draw_cube_vertices, 6, 4, c_paint)
        DrawLinePairs(canvas, draw_cube_vertices, 0, 4, c_paint)
        DrawLinePairs(canvas, draw_cube_vertices, 1, 5, c_paint)
        DrawLinePairs(canvas, draw_cube_vertices, 2, 6, c_paint)
        DrawLinePairs(canvas, draw_cube_vertices, 3, 7, c_paint)
    }

    //*********************************
    //matrix and transformation functions
    fun GetIdentityMatrix(): DoubleArray { //return an 4x4 identity matrix
        val matrix = DoubleArray(16)
        matrix[0] = 1.0
        matrix[1] = 0.0
        matrix[2] = 0.0
        matrix[3] = 0.0
        matrix[4] = 0.0
        matrix[5] = 1.0
        matrix[6] = 0.0
        matrix[7] = 0.0
        matrix[8] = 0.0
        matrix[9] = 0.0
        matrix[10] = 1.0
        matrix[11] = 0.0
        matrix[12] = 0.0
        matrix[13] = 0.0
        matrix[14] = 0.0
        matrix[15] = 1.0
        return matrix
    }

    fun Transformation(
        vertex: Coordinates, matrix: DoubleArray
    ): Coordinates { //affine transformation with homogeneous coordinates
        //i.e. a vector (vertex) multiply with the transformation matrix
        // vertex - vector in 3D
        // matrix - transformation matrix
        val result = Coordinates(
            x = matrix[0] * vertex.x + matrix[1] * vertex.y + matrix[2] * vertex.z + matrix[3],
            y = matrix[4] * vertex.x + matrix[5] * vertex.y + matrix[6] * vertex.z + matrix[7],
            z = matrix[8] * vertex.x + matrix[9] * vertex.y + matrix[10] * vertex.z + matrix[11],
            w = matrix[12] * vertex.x + matrix[13] * vertex.y + matrix[14] * vertex.z + matrix[15]
        )

        return result
    }


    fun transformation(
        vertices: ArrayList<Coordinates>, matrix: DoubleArray
    ): ArrayList<Coordinates> {   //Affine transform a 3D object with vertices
        // vertices - vertices of the 3D object.
        // matrix - transformation matrix
        val result = ArrayList<Coordinates>(vertices.size)
        for (i in vertices.indices) {
            result.add(i, Transformation(vertices[i]!!, matrix!!))
            result[i].normalize()
        }
        return result
    }


    //***********************************************************
    //Affine transformation
    fun translate(
        vertices: ArrayList<Coordinates>, tx: Double, ty: Double, tz: Double
    ): ArrayList<Coordinates> {
        val matrix = GetIdentityMatrix()
        matrix!![3] = tx
        matrix[7] = ty
        matrix[11] = tz
        return transformation(vertices!!, matrix)
    }

    private fun scale(
        vertices: ArrayList<Coordinates>, sx: Double, sy: Double, sz: Double
    ): ArrayList<Coordinates> {
        val matrix = GetIdentityMatrix()
        matrix!![0] = sx
        matrix[5] = sy
        matrix[10] = sz
        return transformation(vertices!!, matrix)
    }


    fun QuaternionRotate(
        vertices: ArrayList<Coordinates>,
        theta: Double,
        abtX: Int,
        abtY: Int,
        abtZ: Int
    ): ArrayList<Coordinates> {
        val w = Math.cos(Math.toRadians(theta / 2.0))
        val sin = Math.sin(Math.toRadians(theta / 2.0))
        val d = abtX.toDouble()
        java.lang.Double.isNaN(d)
        val x = sin * d
        val sin2 = Math.sin(Math.toRadians(theta / 2.0))
        val d2 = abtY.toDouble()
        java.lang.Double.isNaN(d2)
        val y = sin2 * d2
        val sin3 = Math.sin(Math.toRadians(theta / 2.0))
        val d3 = abtZ.toDouble()
        java.lang.Double.isNaN(d3)
        val z = sin3 * d3
        val matrix: DoubleArray = GetIdentityMatrix()
        matrix[0] = w * w + x * x - y * y - z * z
        matrix[1] = x * 2.0 * y - w * 2.0 * z
        matrix[2] = x * 2.0 * z + w * 2.0 * y
        matrix[3] = 0.0
        matrix[4] = x * 2.0 * y + w * 2.0 * z
        matrix[5] = w * w + y * y - x * x - z * z
        matrix[6] = y * 2.0 * z - w * 2.0 * x
        matrix[7] = 0.0
        matrix[8] = x * 2.0 * z - w * 2.0 * y
        matrix[9] = y * 2.0 * z + 2.0 * w * x
        matrix[10] = w * w + z * z - x * x - y * y
        matrix[11] = 0.0
        matrix[12] = 0.0
        matrix[13] = 0.0
        matrix[14] = 0.0
        matrix[15] = 1.0
        return transformation(vertices, matrix)
    }
}