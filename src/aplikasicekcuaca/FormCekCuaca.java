package aplikasicekcuaca;

import org.json.JSONArray;
import org.json.JSONObject;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.LocalDateTime;
import java.util.*;

public class FormCekCuaca extends javax.swing.JFrame {
    private static final String API_KEY = "GANTI_DENGAN_API_KEY_ANDA"; // 🔑 ganti dengan API key kamu
    private final File favFile = new File("favorites.txt");
    private final File csvFile = new File("weather_saved.csv");
    private DefaultTableModel model;

    public FormCekCuaca() {
        initComponents();
        model = (DefaultTableModel) tblData.getModel();
        loadFavorites();
    }

    @SuppressWarnings("unchecked")
    private void initComponents() {

        lblKota = new javax.swing.JLabel();
        txtKota = new javax.swing.JTextField();
        lblFavorit = new javax.swing.JLabel();
        comboFavorit = new javax.swing.JComboBox<>();
        btnCekCuaca = new javax.swing.JButton();
        btnTambahFav = new javax.swing.JButton();
        lblSuhu = new javax.swing.JLabel();
        lblKondisi = new javax.swing.JLabel();
        lblKelembapan = new javax.swing.JLabel();
        lblAngin = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblData = new javax.swing.JTable();
        btnSimpan = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Aplikasi Cek Cuaca");

        lblKota.setText("Kota:");
        lblFavorit.setText("Favorit:");

        comboFavorit.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { }));

        btnCekCuaca.setText("Cek Cuaca");
        btnCekCuaca.addActionListener(evt -> btnCekCuacaActionPerformed(evt));

        btnTambahFav.setText("Tambah Favorit");
        btnTambahFav.addActionListener(evt -> btnTambahFavActionPerformed(evt));

        lblSuhu.setText("Suhu: - °C");
        lblKondisi.setText("Kondisi: -");
        lblKelembapan.setText("Kelembapan: - %");
        lblAngin.setText("Kecepatan angin: - m/s");

        tblData.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {},
            new String [] { "Waktu", "Kota", "Suhu (°C)", "Kondisi", "Kelembapan (%)", "Angin (m/s)" }
        ));
        jScrollPane1.setViewportView(tblData);

        btnSimpan.setText("Simpan ke CSV");
        btnSimpan.addActionListener(evt -> btnSimpanActionPerformed(evt));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(lblKota)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtKota, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(lblFavorit)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(comboFavorit, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(btnCekCuaca)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(btnTambahFav))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lblSuhu)
                            .addComponent(lblKondisi)
                            .addComponent(lblKelembapan)
                            .addComponent(lblAngin))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 450, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(250, 250, 250)
                        .addComponent(btnSimpan)))
                .addContainerGap(50, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblKota)
                    .addComponent(txtKota, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblFavorit)
                    .addComponent(comboFavorit, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnCekCuaca)
                    .addComponent(btnTambahFav))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(lblSuhu)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(lblKondisi)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(lblKelembapan)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(lblAngin))
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btnSimpan)
                .addContainerGap(20, Short.MAX_VALUE))
        );

        pack();
    }

    // --- Tombol CEK CUACA ---
    private void btnCekCuacaActionPerformed(java.awt.event.ActionEvent evt) {
        String kota = txtKota.getText().trim();
        if (kota.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Masukkan nama kota terlebih dahulu.");
            return;
        }

        try {
            JSONObject json = getWeather(kota);
            JSONObject main = json.getJSONObject("main");
            double suhu = main.getDouble("temp");
            int kelembapan = main.getInt("humidity");
            JSONObject wind = json.getJSONObject("wind");
            double angin = wind.getDouble("speed");

            JSONArray weather = json.getJSONArray("weather");
            JSONObject w0 = weather.getJSONObject(0);
            String kondisi = w0.getString("description");

            lblSuhu.setText("Suhu: " + suhu + " °C");
            lblKondisi.setText("Kondisi: " + kondisi);
            lblKelembapan.setText("Kelembapan: " + kelembapan + " %");
            lblAngin.setText("Kecepatan angin: " + angin + " m/s");

            model.addRow(new Object[]{
                LocalDateTime.now().toString(), kota, suhu, kondisi, kelembapan, angin
            });

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Gagal mengambil data: " + ex.getMessage());
        }
    }

    // --- Tombol TAMBAH FAVORIT ---
    private void btnTambahFavActionPerformed(java.awt.event.ActionEvent evt) {
        String kota = txtKota.getText().trim();
        if (kota.isEmpty()) return;
        boolean exists = false;
        for (int i = 0; i < comboFavorit.getItemCount(); i++) {
            if (comboFavorit.getItemAt(i).equalsIgnoreCase(kota)) {
                exists = true;
                break;
            }
        }
        if (!exists) {
            comboFavorit.addItem(kota);
            saveFavorites();
            JOptionPane.showMessageDialog(this, "Kota ditambahkan ke favorit!");
        } else {
            JOptionPane.showMessageDialog(this, "Kota sudah ada di favorit.");
        }
    }

    // --- Tombol SIMPAN CSV ---
    private void btnSimpanActionPerformed(java.awt.event.ActionEvent evt) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(csvFile))) {
            writer.println("Waktu,Kota,Suhu (°C),Kondisi,Kelembapan (%),Angin (m/s)");
            for (int i = 0; i < model.getRowCount(); i++) {
                for (int j = 0; j < model.getColumnCount(); j++) {
                    writer.print(model.getValueAt(i, j));
                    if (j < model.getColumnCount() - 1) writer.print(",");
                }
                writer.println();
            }
            JOptionPane.showMessageDialog(this, "Data berhasil disimpan ke " + csvFile.getAbsolutePath());
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Gagal menyimpan CSV: " + e.getMessage());
        }
    }

    // --- Fungsi bantu untuk ambil data cuaca ---
    private JSONObject getWeather(String kota) throws Exception {
        String urlString = "https://api.openweathermap.org/data/2.5/weather?q=" + kota + "&units=metric&appid=" + API_KEY;
        HttpURLConnection conn = (HttpURLConnection) new URL(urlString).openConnection();
        conn.setRequestMethod("GET");
        BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        StringBuilder response = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) response.append(line);
        reader.close();
        return new JSONObject(response.toString());
    }

    // --- Simpan & muat favorit ---
    private void saveFavorites() {
        try (PrintWriter pw = new PrintWriter(new FileWriter(favFile))) {
            for (int i = 0; i < comboFavorit.getItemCount(); i++) {
                pw.println(comboFavorit.getItemAt(i));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void loadFavorites() {
        if (!favFile.exists()) return;
        try (Scanner sc = new Scanner(favFile)) {
            while (sc.hasNextLine()) comboFavorit.addItem(sc.nextLine());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // --- Main ---
    public static void main(String[] args) {
        java.awt.EventQueue.invokeLater(() -> {
            FormCekCuaca frame = new FormCekCuaca();
            frame.setVisible(true);
            frame.setLocationRelativeTo(null);
        });
    }

    // Variabel GUI
    private javax.swing.JButton btnCekCuaca;
    private javax.swing.JButton btnTambahFav;
    private javax.swing.JButton btnSimpan;
    private javax.swing.JComboBox<String> comboFavorit;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel lblKota;
    private javax.swing.JLabel lblFavorit;
    private javax.swing.JLabel lblSuhu;
    private javax.swing.JLabel lblKondisi;
    private javax.swing.JLabel lblKelembapan;
    private javax.swing.JLabel lblAngin;
    private javax.swing.JTable tblData;
    private javax.swing.JTextField txtKota;
}
