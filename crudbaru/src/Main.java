import java.io.*;
import java.util.*;

class Mahasiswa {
    private String nama;
    private final String nim;
    private String alamat;
    private String semester;
    private String sks;
    private String ipk;

    public Mahasiswa(String nama, String nim, String alamat, String semester, String sks, String ipk) {
        this.nama = nama;
        this.nim = nim;
        this.alamat = alamat;
        this.semester = semester;
        this.sks = sks;
        this.ipk = ipk;
    }

    public String getNim() {
        return nim;
    }

    public void update(String nama, String alamat, String semester, String sks, String ipk) {
        this.nama = nama;
        this.alamat = alamat;
        this.semester = semester;
        this.sks = sks;
        this.ipk = ipk;
    }

    @Override
    public String toString() {
        return nama + "," + nim + "," + alamat + "," + semester + "," + sks + "," + ipk;
    }
}

class Database {
    private static final String FILE_NAME = "database.csv";
    private final List<Mahasiswa> mahasiswaList = new ArrayList<>();

    public void load() throws IOException {
        mahasiswaList.clear();
        try (BufferedReader br = new BufferedReader(new FileReader(FILE_NAME))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] data = line.split(",");
                mahasiswaList.add(new Mahasiswa(data[0], data[1], data[2], data[3], data[4], data[5]));
            }
        }
    }

    public void save() throws IOException {
        try (PrintWriter pw = new PrintWriter(new FileWriter(FILE_NAME))) {
            for (Mahasiswa mhs : mahasiswaList) {
                pw.println(mhs);
            }
        }
    }

    public void add(Mahasiswa mhs) throws IOException {
        mahasiswaList.add(mhs);
        save();
    }

    public void display() {
        System.out.println("\n+-------------------------------------------------------------------------------------+");
        System.out.printf("| %-20s | %-10s | %-20s | %-8s | %-5s | %-5s |%n", "Nama", "NIM", "Alamat", "Semester", "SKS", "IPK");
        System.out.println("+-------------------------------------------------------------------------------------+");
        for (Mahasiswa mhs : mahasiswaList) {
            String[] data = mhs.toString().split(",");
            System.out.printf("| %-20s | %-10s | %-20s | %-8s | %-5s | %-5s |%n", data[0], data[1], data[2], data[3], data[4], data[5]);
        }
        System.out.println("+-------------------------------------------------------------------------------------+");
    }

    public Mahasiswa findByNim(String nim) {
        for (Mahasiswa mhs : mahasiswaList) {
            if (mhs.getNim().equals(nim)) {
                return mhs;
            }
        }
        return null;
    }

    public void update(String nim, Mahasiswa newData) throws IOException {
        Mahasiswa mhs = findByNim(nim);
        if (mhs != null) {
            mhs.update(newData.toString().split(",")[0], newData.toString().split(",")[2], newData.toString().split(",")[3], newData.toString().split(",")[4], newData.toString().split(",")[5]);
            save();
        }
    }

    public boolean delete(String nim) throws IOException {
        if (mahasiswaList.removeIf(mhs -> mhs.getNim().equals(nim))) {
            save();
            return true;
        }
        return false;
    }
}

public class Main {
    public static void main(String[] args) throws IOException {
        Scanner scanner = new Scanner(System.in);
        Database db = new Database();
        db.load();

        boolean running = true;
        while (running) {
            System.out.println("=====================");
            System.out.println("|   Pilih menu:     |");
            System.out.println("|   [C] : Create    |");
            System.out.println("|   [R] : Read      |");
            System.out.println("|   [U] : Update    |");
            System.out.println("|   [D] : Delete    |");
            System.out.println("|   [X] : Exit      |");
            System.out.println("=====================");
            System.out.print("Masukkan pilihan: ");
            String choice = scanner.nextLine().toUpperCase();

            switch (choice) {
                case "C":
                    System.out.print("Nama: ");
                    String nama = scanner.nextLine();
                    System.out.print("NIM: ");
                    String nim = scanner.nextLine();
                    System.out.print("Alamat: ");
                    String alamat = scanner.nextLine();
                    System.out.print("Semester: ");
                    String semester = scanner.nextLine();
                    System.out.print("SKS: ");
                    String sks = scanner.nextLine();
                    System.out.print("IPK: ");
                    String ipk = scanner.nextLine();
                    db.add(new Mahasiswa(nama, nim, alamat, semester, sks, ipk));
                    System.out.println("Data berhasil ditambahkan!");
                    break;
                case "R":
                    db.display();
                    break;
                case "U":
                    db.display();
                    System.out.print("Masukkan NIM yang akan diupdate: ");
                    String targetNim = scanner.nextLine();
                    Mahasiswa mhs = db.findByNim(targetNim);
                    if (mhs != null) {
                        System.out.print("Nama baru: ");
                        String namaBaru = scanner.nextLine();
                        System.out.print("Alamat baru: ");
                        String alamatBaru = scanner.nextLine();
                        System.out.print("Semester baru: ");
                        String semesterBaru = scanner.nextLine();
                        System.out.print("SKS baru: ");
                        String sksBaru = scanner.nextLine();
                        System.out.print("IPK baru: ");
                        String ipkBaru = scanner.nextLine();
                        db.update(targetNim, new Mahasiswa(namaBaru, targetNim, alamatBaru, semesterBaru, sksBaru, ipkBaru));
                        System.out.println("Data berhasil diperbarui!");
                    } else {
                        System.out.println("NIM tidak ditemukan.");
                    }
                    break;
                case "D":
                    System.out.print("Masukkan NIM yang akan dihapus: ");
                    if (db.delete(scanner.nextLine())) {
                        System.out.println("Data berhasil dihapus!");
                    } else {
                        System.out.println("NIM tidak ditemukan.");
                    }
                    break;
                case "X":
                    running = false;
                    break;
                default:
                    System.out.println("Pilihan tidak valid.");
            }
        }
        scanner.close();
    }
}
