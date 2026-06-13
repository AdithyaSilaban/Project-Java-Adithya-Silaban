package model;

/**
 * [KONSEP: INTERFACE - multiple interface]
 * Java tidak mendukung multiple inheritance class, tapi mendukung multiple interface.
 * Dengan memisahkan kontrak, kita menerapkan Interface Segregation Principle (ISP).
 */
public interface Displayable {
    /**
     * Setiap entitas yang bisa "ditampilkan" harus mengimplementasikan metode ini.
     * Ini adalah pondasi dari Polymorphism berbasis interface.
     */
    void tampilkanInfo();
}
