
import kotlin.math.abs

class PhanSo(var tu: Int, var mau: Int) {

    // Hàm nhập phân số
    fun nhap() {
        while (true) {
            print("Nhập tử số: ")
            tu = readln().toInt()
            print("Nhập mẫu số (khác 0): ")
            mau = readln().toInt()
            if (mau != 0) break
            println("Mẫu số phải khác 0, vui lòng nhập lại!")
        }
    }

    // In phân số
    fun xuat() {
        println("$tu/$mau")
    }

    // Tối giản phân số
    fun toiGian() {
        val gcd = gcd(abs(tu), abs(mau))
        tu /= gcd
        mau /= gcd
        if (mau < 0) { 
            tu = -tu
            mau = -mau
        }
    }

    // Hàm so sánh với phân số khác
    fun soSanh(ps: PhanSo): Int {
        val left = tu.toLong() * ps.mau
        val right = ps.tu.toLong() * mau
        return when {
            left < right -> -1
            left == right -> 0
            else -> 1
        }
    }

    // Tính tổng với một phân số khác
    fun cong(ps: PhanSo): PhanSo {
        val tuMoi = tu * ps.mau + ps.tu * mau
        val mauMoi = mau * ps.mau
        val kq = PhanSo(tuMoi, mauMoi)
        kq.toiGian()
        return kq
    }

    // Hàm gcd
    private fun gcd(a: Int, b: Int): Int {
        return if (b == 0) a else gcd(b, a % b)
    }
}

fun main() {
    print("Nhập số lượng phân số: ")
    val n = readln().toInt()
    val arr = Array(n) { PhanSo(0, 1) }

    // Nhập mảng phân số
    for (i in arr.indices) {
        println("Nhập phân số thứ ${i + 1}:")
        arr[i].nhap()
    }

    println("\nMảng phân số vừa nhập:")
    arr.forEach { it.xuat() }

    println("\nMảng sau khi tối giản:")
    arr.forEach {
        it.toiGian()
        it.xuat()
    }

    // Tính tổng
    var tong = PhanSo(0, 1)
    arr.forEach { tong = tong.cong(it) }
    print("\nTổng các phân số = ")
    tong.xuat()

    // Tìm max
    var max = arr[0]
    for (i in 1 until arr.size) {
        if (arr[i].soSanh(max) == 1) max = arr[i]
    }
    print("\nPhân số lớn nhất là: ")
    max.xuat()

    // Sắp xếp giảm dần
    val sapXep = arr.sortedWith { a, b -> b.soSanh(a) }
    println("\nMảng sau khi sắp xếp giảm dần:")
    sapXep.forEach { it.xuat() }
}

