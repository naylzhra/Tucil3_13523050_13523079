# Rush Hour Solver

> **IF2211 – Strategi Algoritma**
> Tugas Kecil 3 - Semester II / 2024-2025

## Authors
* Mayla Yaffa Ludmilla (13523050)
* Nayla Zahira (13523079)

### Deskripsi Singkat

Program ini menyelesaikan puzzle **Rush Hour** (ukuran papan bebas) menggunakan empat algoritma pencarian jalur :

| Algoritma                            | Deskripsi singkat                                                                |
| ------------------------------------ | -------------------------------------------------------------------------------- |
| **Greedy Best First Search (GBFS)**  | Memilih simpul dengan `h(n)` terendah.                                           |
| **Uniform Cost Search (UCS)**        | Memilih simpul dengan `g(n)` terendah.                                           |
| **A\***                              | Memilih simpul dengan `f(n)=g(n)+h(n)`. Heuristik default : *blocking-distance*. |
| **Iterative Deepening Search (IDS)** | DFS dengan batas kedalaman yang di-iterasi.                                      |

Output : jumlah node yang dikunjungi, waktu eksekusi, serta urutan konfigurasi papan dari awal hingga solusi. Konfigurasi papann dapat dilihat di terminal maupun GUI.

---
## Technologies Used
* Java
* Maven
* JavaFX

## Requirement
* Java 17+
* Maven 3.9+ 
* Terminal yang mendukung ANSI color (Windows 10 + Windows Terminal / PowerShell, macOS Terminal, Linux shell).

## Cara Kompilasi

#### 1. Clone repository
```bash
git clone https://github.com/naylzhra/Tucil3_13523050_13523079.git    
```
#### 2. Masuk ke repository di local, lalu jalankan
```bash
mvn clean javafx:run
```
#### 3. (Opsional) Untuk menjalankan program dengan mode CLI, jalankan
```bash
./run.bat
```

### Format File Input

```
A B            # dimensi papan
N              # banyak piece non-primary
<baris-papan>  # konfigurasi papan
```

Contoh 5 × 7 :

```
5 7
10
  K
AA..BBB
.EPCCC.
.EP...F
.GHH..F
.G.JJJ.
```

## Struktur Repository

```
.
├── src/         # kode sumber Java
│   ├── algo/    # A*, UCS, GBFS, IDS, SolveResult
│   ├── object/  # Board, Piece, Node
│   ├── utils/   # Input parser, TxtOutputWriter
│   └── cli/     # Main (console)
├── test/
│   ├── input/   # contoh papan .txt
│   └── output/  # solusi yang dihasilkan
├── doc/         # laporan PDF
└── README.md
```
## Contributing
Apabila Anda ingin berkontribusi dalam projek ini, silakan fork repository ini dan gunakan feature branch. Pull requests akan diterima dengan hangat.

Institut Teknologi Bandung – 2025

