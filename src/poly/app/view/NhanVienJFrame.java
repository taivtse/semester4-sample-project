/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package poly.app.view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ButtonGroup;
import javax.swing.JLabel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import poly.app.core.constant.CoreConstant;
import poly.app.core.dao.NhanVienDao;
import poly.app.core.daoimpl.NhanVienDaoImpl;
import poly.app.core.entities.NhanVien;
import poly.app.core.helper.DateHelper;
import poly.app.core.helper.ObjectStructureHelper;
import poly.app.core.helper.DialogHelper;
import poly.app.core.utils.DataFactoryUtil;
import poly.app.core.utils.EMailUtil;
import poly.app.core.utils.StringUtil;

public class NhanVienJFrame extends javax.swing.JFrame {

    NhanVienDao nhanVienDao = new NhanVienDaoImpl();
    Vector<Vector> tableData = new Vector<>();
    HashMap<String, NhanVien> nhanVienHashMap = new HashMap<>();
    int selectedIndex = -1;
    boolean isDataLoaded = false;

    public NhanVienJFrame() {
        initComponents();
        setLocationRelativeTo(null);
        reRenderUI();
        addRadioToGroup();
        loadDataToTable();
    }

    private void reRenderUI() {
        DefaultTableModel tableModel = (DefaultTableModel) tblNhanVien.getModel();
        tableModel = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tableModel.setDataVector(tableData, new Vector(Arrays.asList(ObjectStructureHelper.NHANVIEN_TABLE_IDENTIFIERS)));
        tblNhanVien.setModel(tableModel);

        JTableHeader jTableHeader = tblNhanVien.getTableHeader();
        jTableHeader.setFont(new Font("open sans", Font.PLAIN, 14)); // font name style size
        // canh giua man hinh
        ((DefaultTableCellRenderer) jTableHeader.getDefaultRenderer())
                .setHorizontalAlignment(JLabel.CENTER);
        // chieu cao header
        jTableHeader.setPreferredSize(new Dimension(0, 25));
        jTableHeader.setForeground(Color.decode("#000")); // change the Foreground
        tblNhanVien.setFillsViewportHeight(true);
        tblNhanVien.setBackground(Color.WHITE);

        DefaultTableCellRenderer cellLeft = new DefaultTableCellRenderer();
        cellLeft.setHorizontalAlignment(JLabel.LEFT);

        DefaultTableCellRenderer cellCenter = new DefaultTableCellRenderer();
        cellCenter.setHorizontalAlignment(JLabel.CENTER);

        DefaultTableCellRenderer cellRight = new DefaultTableCellRenderer();
        cellRight.setHorizontalAlignment(JLabel.RIGHT);

        tblNhanVien.getColumnModel().getColumn(0).setCellRenderer(cellLeft);
        tblNhanVien.getColumnModel().getColumn(1).setCellRenderer(cellLeft);
        tblNhanVien.getColumnModel().getColumn(2).setCellRenderer(cellRight);
        tblNhanVien.getColumnModel().getColumn(3).setCellRenderer(cellRight);
        tblNhanVien.getColumnModel().getColumn(4).setCellRenderer(cellCenter);

        // set width for column
        double tableWidth = tblNhanVien.getPreferredSize().getWidth();
        tblNhanVien.getColumnModel().getColumn(1).setPreferredWidth((int) (tableWidth * 0.35));
        tblNhanVien.getColumnModel().getColumn(3).setPreferredWidth((int) (tableWidth * 0.45));

//        Add data to combobox
        for (String identifier : ObjectStructureHelper.NHANVIEN_TABLE_IDENTIFIERS) {
            cboBoLoc.addItem(identifier);
        }
        cboBoLoc.setSelectedIndex(1);
    }

    private void addRadioToGroup() {
        gioiTinhGroup.add(rdoNam);
        gioiTinhGroup.add(rdoNu);

        vaiTroGroup.add(rdoTruongPhong);
        vaiTroGroup.add(rdoNhanVien);
    }

    private void loadDataToTable() {
        tableData.clear();
        List<NhanVien> dataLoadedList = new NhanVienDaoImpl().selectByProperties(null, null, "vaiTro", CoreConstant.SORT_DESC, null, null);

        try {
            for (NhanVien nhanVien : dataLoadedList) {
//                Convert nhanvien to vector
                Vector vData = DataFactoryUtil.objectToVectorByFields(nhanVien, ObjectStructureHelper.NHANVIEN_PROPERTIES);
                int fieldVaiTro = vData.size() - 1;
                boolean isTruongPhong = (boolean) vData.get(fieldVaiTro);
                vData.set(fieldVaiTro, isTruongPhong ? "Trưởng phòng" : "Nhân viên");
                tableData.add(vData);

//                Add nhanvien to hashmap
                nhanVienHashMap.put(nhanVien.getMaNv(), nhanVien);
            }
            tblNhanVien.updateUI();
        } catch (Exception ex) {
            Logger.getLogger(NhanVienJFrame.class.getName()).log(Level.SEVERE, null, ex);
        }

        isDataLoaded = true;
    }

    private void changeSelectedIndex() {
        if (selectedIndex >= 0) {
            tblNhanVien.setRowSelectionInterval(selectedIndex, selectedIndex);
            setModelToForm();
        } else {
            resetForm();
        }
    }

    private void setModelToForm() {
        if (selectedIndex == -1) {
            return;
        }
//        get ma nhan vien by selected index
        String maNv = tblNhanVien.getValueAt(selectedIndex, 0).toString();
        NhanVien selectedNhanVien = nhanVienHashMap.get(maNv);
        txtMaNhanVien.setText(selectedNhanVien.getMaNv());
        txtHoTen.setText(selectedNhanVien.getHoTen());
        txtNgaySinh.setText(DateHelper.toString(selectedNhanVien.getNgaySinh()));
        txtSoDienThoai.setText(selectedNhanVien.getDienThoai());
        txtEmail.setText(selectedNhanVien.getEmail());
        txtDiaChi.setText(selectedNhanVien.getDiaChi());

        if (selectedNhanVien.getVaiTro()) {
            rdoTruongPhong.setSelected(true);
        } else {
            rdoNhanVien.setSelected(true);
        }

        if (selectedNhanVien.getGioiTinh()) {
            rdoNam.setSelected(true);
        } else {
            rdoNu.setSelected(true);
        }
    }

    private void resetForm() {
        txtMaNhanVien.setText("");
        txtHoTen.setText("");
        txtNgaySinh.setText("");
        txtSoDienThoai.setText("");
        txtEmail.setText("");
        txtDiaChi.setText("");
        txtMaNhanVien.requestFocus();

        rdoNam.setSelected(true);
        rdoTruongPhong.setSelected(true);
    }

    private void addModelToTable(NhanVien nhanVien) {
        Vector vData = new Vector();
        vData.add(nhanVien.getMaNv());
        vData.add(nhanVien.getHoTen());
        vData.add(nhanVien.getDienThoai());
        vData.add(nhanVien.getEmail());
        vData.add(nhanVien.getVaiTro() ? "Trưởng phòng" : "Nhân viên");

        tableData.add(vData);
    }

    private NhanVien getModelFromForm() {
        NhanVien nhanVien = new NhanVien();
        nhanVien.setMaNv(txtMaNhanVien.getText());
        nhanVien.setHoTen(txtHoTen.getText());
        nhanVien.setNgaySinh(DateHelper.toDate(txtNgaySinh.getText()));
        nhanVien.setGioiTinh(rdoNam.isSelected());
        nhanVien.setDienThoai(txtSoDienThoai.getText());
        nhanVien.setEmail(txtEmail.getText());
        nhanVien.setDiaChi(txtDiaChi.getText());
        nhanVien.setVaiTro(rdoTruongPhong.isSelected());

        return nhanVien;
    }

    private void sendUserInfoToEmail(NhanVien nhanVien) {
        new Thread(() -> {
            String matKhau = nhanVien.getMatKhau();
            String msgSubject = "Thông tin tài khoản";
            String msgBody = "<h2 style='color: #B93B2D'>Xin chào!<br>Dưới đây là thông tin tài khoản của bạn.</h2>"
                    + "<br>Mã nhân viên: <b>" + nhanVien.getMaNv()
                    + "</b><br>Mật khẩu: <b>" + matKhau
                    + "</b><br>Sử dụng tài khoản và mật khẩu trên để đăng nhập vào hệ thống.";
            new EMailUtil(nhanVien.getEmail(), msgBody, msgSubject).sendMail();
        }).start();
    }

    private boolean validateInputAddingState() {
        for (NhanVien nhanVien : nhanVienHashMap.values()) {
            if (nhanVien.getMaNv().equals(txtMaNhanVien.getText())) {
                DialogHelper.message(this, "Mã nhân viên đã tồn tại", DialogHelper.ERROR_MESSAGE);
                return false;
            } else if (nhanVien.getEmail().equals(txtEmail.getText())) {
                DialogHelper.message(this, "Email đã tồn tại", DialogHelper.ERROR_MESSAGE);
                return false;
            }
        }

//        TODO: validate here
        return true;
    }

    private boolean validateInputEditingState() {
        String maNv = tblNhanVien.getValueAt(selectedIndex, 0).toString();
        if (!nhanVienHashMap.get(maNv).getEmail().equals(txtEmail.getText())) {
            for (NhanVien nhanVien : nhanVienHashMap.values()) {
                if (nhanVien.getEmail().equals(txtEmail.getText())) {
                    DialogHelper.message(this, "Email đã tồn tại", DialogHelper.ERROR_MESSAGE);
                    return false;
                }
            }
        }

//        TODO: validate here
        return true;
    }

    private void setAddingState() {
        txtMaNhanVien.setEditable(true);
        txtMaNhanVien.requestFocus();
        btnThem.setEnabled(true);
        btnLamMoi.setEnabled(true);
        btnCapNhat.setEnabled(false);
        btnXoa.setEnabled(false);
    }

    private void setEditingState() {
        txtMaNhanVien.setEditable(false);
        btnThem.setEnabled(false);
        btnLamMoi.setEnabled(true);
        btnCapNhat.setEnabled(true);
        btnXoa.setEnabled(true);
    }

    private void insertModel() {
        if (validateInputAddingState()) {
            String randomPassword = "$$" + StringUtil.randomString();
            NhanVien nhanVien = getModelFromForm();
            nhanVien.setMatKhau(randomPassword);
            boolean isInserted = new NhanVienDaoImpl().insert(nhanVien);

            if (isInserted) {
                sendUserInfoToEmail(nhanVien);
                DialogHelper.message(this, "Thêm dữ liệu thành công.\nKiểm tra email để nhận thông tin đăng nhập.", DialogHelper.INFORMATION_MESSAGE);
                loadDataToTable();
                tblNhanVien.updateUI();
                setEditingState();
            } else {
                DialogHelper.message(this, "Thêm dữ liệu thất bại.", DialogHelper.ERROR_MESSAGE);
            }
        }
    }

    private void deleteModel() {
        boolean isConfirm = DialogHelper.confirm(this, "Bạn chắc chắn muốn xoá?");
        if (isConfirm) {
            String maNv = tblNhanVien.getValueAt(selectedIndex, 0).toString();
            NhanVien nhanVien = nhanVienHashMap.get(maNv);
            boolean isDeleted = new NhanVienDaoImpl().delete(nhanVien);

            if (isDeleted) {
                DialogHelper.message(this, "Xoá dữ liệu thành công.", DialogHelper.INFORMATION_MESSAGE);
                loadDataToTable();
                tblNhanVien.updateUI();
                changeSelectedIndex();
            } else {
                DialogHelper.message(this, "Xoá dữ liệu thất bại.", DialogHelper.ERROR_MESSAGE);
            }
        }
    }

    private void updateModel() {
        if (validateInputEditingState()) {
            String maNv = tblNhanVien.getValueAt(selectedIndex, 0).toString();
            NhanVien nhanVienOldData = nhanVienHashMap.get(maNv);

            NhanVien nhanVienNewData = getModelFromForm();
            boolean isUpdated = false;
            try {
                nhanVienNewData = DataFactoryUtil.mergeTwoObject(nhanVienOldData, nhanVienNewData);
                nhanVienHashMap.put(maNv, nhanVienNewData);

                isUpdated = new NhanVienDaoImpl().update(nhanVienNewData);
            } catch (Exception ex) {
                Logger.getLogger(NhanVienJFrame.class.getName()).log(Level.SEVERE, null, ex);
            }

            if (isUpdated) {
                Vector vData;
                try {
                    vData = DataFactoryUtil.objectToVectorByFields(nhanVienNewData, ObjectStructureHelper.NHANVIEN_PROPERTIES);
                    int fieldVaiTro = vData.size() - 1;
                    boolean isTruongPhong = (boolean) vData.get(fieldVaiTro);
                    vData.set(fieldVaiTro, isTruongPhong ? "Trưởng phòng" : "Nhân viên");

//                    Find index of updated NhanVien in tabledata
                    for (int i = 0; i < tableData.size(); i++) {
                        if (tableData.get(i).get(0).equals(vData.get(0))) {
                            tableData.set(i, vData);
                            break;
                        }
                    }

                    tblNhanVien.updateUI();
                    DialogHelper.message(this, "Cập nhật dữ liệu thành công.", DialogHelper.INFORMATION_MESSAGE);
                } catch (Exception ex) {
                    Logger.getLogger(NhanVienJFrame.class.getName()).log(Level.SEVERE, null, ex);
                }
            } else {
                DialogHelper.message(this, "Cập nhật dữ liệu thất bại.", DialogHelper.ERROR_MESSAGE);
            }
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel3 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jPanel4 = new javax.swing.JPanel();
        panelTab = new javax.swing.JTabbedPane();
        jPanel2 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblNhanVien = new javax.swing.JTable();
        jPanel1 = new javax.swing.JPanel();
        jPanel5 = new javax.swing.JPanel();
        rdoNam = new javax.swing.JRadioButton();
        rdoNu = new javax.swing.JRadioButton();
        txtNgaySinh = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        txtHoTen = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        txtMaNhanVien = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        txtSoDienThoai = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        txtEmail = new javax.swing.JTextField();
        jLabel9 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        rdoTruongPhong = new javax.swing.JRadioButton();
        rdoNhanVien = new javax.swing.JRadioButton();
        txtDiaChi = new javax.swing.JTextField();
        jPanel7 = new javax.swing.JPanel();
        btnThem = new javax.swing.JButton();
        btnCapNhat = new javax.swing.JButton();
        btnXoa = new javax.swing.JButton();
        btnLamMoi = new javax.swing.JButton();
        btnFirst = new javax.swing.JButton();
        btnPrev = new javax.swing.JButton();
        btnNext = new javax.swing.JButton();
        btnLast = new javax.swing.JButton();
        jPanel6 = new javax.swing.JPanel();
        jPanel8 = new javax.swing.JPanel();
        txtTimKiem = new javax.swing.JTextField();
        cboBoLoc = new javax.swing.JComboBox<>();
        jLabel3 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setBackground(new java.awt.Color(255, 255, 255));
        setPreferredSize(new java.awt.Dimension(850, 610));
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                formWindowClosing(evt);
            }
        });

        jPanel3.setBackground(new java.awt.Color(65, 76, 89));

        jLabel1.setBackground(new java.awt.Color(255, 255, 255));
        jLabel1.setFont(new java.awt.Font("Open Sans", 1, 18)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setText("QUẢN LÝ NHÂN VIÊN");
        jPanel3.add(jLabel1);

        jPanel4.setBackground(new java.awt.Color(255, 255, 255));

        panelTab.setBackground(new java.awt.Color(255, 255, 255));
        panelTab.setFont(new java.awt.Font("Open Sans", 0, 14)); // NOI18N
        panelTab.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                panelTabStateChanged(evt);
            }
        });

        tblNhanVien.setAutoCreateRowSorter(true);
        tblNhanVien.setFont(new java.awt.Font("Open Sans", 0, 14)); // NOI18N
        tblNhanVien.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {

            }
        ));
        tblNhanVien.setGridColor(new java.awt.Color(204, 204, 204));
        tblNhanVien.setIntercellSpacing(new java.awt.Dimension(0, 1));
        tblNhanVien.setRowHeight(23);
        tblNhanVien.setSelectionBackground(new java.awt.Color(65, 76, 89));
        tblNhanVien.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        tblNhanVien.setShowGrid(false);
        tblNhanVien.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblNhanVienMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(tblNhanVien);

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 829, Short.MAX_VALUE)
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 419, Short.MAX_VALUE)
        );

        panelTab.addTab("Danh Sách", jPanel2);

        jPanel5.setFocusTraversalKeysEnabled(false);
        jPanel5.setFocusable(false);
        jPanel5.setPreferredSize(new java.awt.Dimension(408, 390));

        rdoNam.setFont(new java.awt.Font("Open Sans", 0, 14)); // NOI18N
        rdoNam.setSelected(true);
        rdoNam.setText("Nam");

        rdoNu.setFont(new java.awt.Font("Open Sans", 0, 14)); // NOI18N
        rdoNu.setText("Nữ");

        txtNgaySinh.setFont(new java.awt.Font("Open Sans", 0, 14)); // NOI18N

        jLabel7.setFont(new java.awt.Font("Open Sans", 0, 14)); // NOI18N
        jLabel7.setText("Ngày sinh");

        txtHoTen.setFont(new java.awt.Font("Open Sans", 0, 14)); // NOI18N
        txtHoTen.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtHoTenKeyTyped(evt);
            }
        });

        jLabel5.setFont(new java.awt.Font("Open Sans", 0, 14)); // NOI18N
        jLabel5.setText("Họ  và tên");

        txtMaNhanVien.setEditable(false);
        txtMaNhanVien.setFont(new java.awt.Font("Open Sans", 0, 14)); // NOI18N

        jLabel2.setFont(new java.awt.Font("Open Sans", 0, 14)); // NOI18N
        jLabel2.setText("Mã nhân viên");

        jLabel10.setFont(new java.awt.Font("Open Sans", 0, 14)); // NOI18N
        jLabel10.setText("Giới tính");

        txtSoDienThoai.setFont(new java.awt.Font("Open Sans", 0, 14)); // NOI18N
        txtSoDienThoai.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtSoDienThoaiKeyTyped(evt);
            }
        });

        jLabel4.setFont(new java.awt.Font("Open Sans", 0, 14)); // NOI18N
        jLabel4.setText("Số điện thoại");

        jLabel8.setFont(new java.awt.Font("Open Sans", 0, 14)); // NOI18N
        jLabel8.setText("Email");

        txtEmail.setFont(new java.awt.Font("Open Sans", 0, 14)); // NOI18N

        jLabel9.setFont(new java.awt.Font("Open Sans", 0, 14)); // NOI18N
        jLabel9.setText("Địa chỉ");

        jLabel6.setFont(new java.awt.Font("Open Sans", 0, 14)); // NOI18N
        jLabel6.setText("Vai Trò");

        rdoTruongPhong.setFont(new java.awt.Font("Open Sans", 0, 14)); // NOI18N
        rdoTruongPhong.setSelected(true);
        rdoTruongPhong.setText("Trưởng phòng");

        rdoNhanVien.setFont(new java.awt.Font("Open Sans", 0, 14)); // NOI18N
        rdoNhanVien.setText("Nhân viên");

        txtDiaChi.setFont(new java.awt.Font("Open Sans", 0, 14)); // NOI18N

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addGap(30, 30, 30)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel7)
                    .addComponent(jLabel5)
                    .addComponent(txtMaNhanVien, javax.swing.GroupLayout.DEFAULT_SIZE, 360, Short.MAX_VALUE)
                    .addComponent(jLabel2)
                    .addComponent(txtHoTen, javax.swing.GroupLayout.DEFAULT_SIZE, 360, Short.MAX_VALUE)
                    .addComponent(txtNgaySinh, javax.swing.GroupLayout.DEFAULT_SIZE, 360, Short.MAX_VALUE)
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addGap(110, 110, 110)
                        .addComponent(rdoNu))
                    .addComponent(rdoNam)
                    .addComponent(jLabel10))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel4)
                    .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addGroup(jPanel5Layout.createSequentialGroup()
                            .addComponent(rdoTruongPhong)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                            .addComponent(rdoNhanVien))
                        .addComponent(jLabel6)
                        .addComponent(jLabel9)
                        .addComponent(txtSoDienThoai, javax.swing.GroupLayout.DEFAULT_SIZE, 360, Short.MAX_VALUE)
                        .addComponent(txtDiaChi))
                    .addComponent(txtEmail, javax.swing.GroupLayout.PREFERRED_SIZE, 360, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel8))
                .addGap(30, 30, 30))
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addGap(25, 25, 25)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtMaNhanVien, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtHoTen, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtNgaySinh, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jLabel10, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(rdoNam)
                            .addComponent(rdoNu)))
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtSoDienThoai, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtEmail, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jLabel9, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtDiaChi, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(rdoTruongPhong)
                            .addComponent(rdoNhanVien))))
                .addContainerGap(39, Short.MAX_VALUE))
        );

        jPanel7.setBackground(new java.awt.Color(245, 245, 245));

        btnThem.setFont(new java.awt.Font("Open Sans", 0, 14)); // NOI18N
        btnThem.setText("Thêm");
        btnThem.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnThem.setFocusTraversalKeysEnabled(false);
        btnThem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnThemActionPerformed(evt);
            }
        });

        btnCapNhat.setFont(new java.awt.Font("Open Sans", 0, 14)); // NOI18N
        btnCapNhat.setText("Cập nhật");
        btnCapNhat.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnCapNhat.setEnabled(false);
        btnCapNhat.setFocusTraversalKeysEnabled(false);
        btnCapNhat.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCapNhatActionPerformed(evt);
            }
        });

        btnXoa.setFont(new java.awt.Font("Open Sans", 0, 14)); // NOI18N
        btnXoa.setText("Xóa");
        btnXoa.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnXoa.setEnabled(false);
        btnXoa.setFocusTraversalKeysEnabled(false);
        btnXoa.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnXoaActionPerformed(evt);
            }
        });

        btnLamMoi.setFont(new java.awt.Font("Open Sans", 0, 14)); // NOI18N
        btnLamMoi.setText("Làm mới");
        btnLamMoi.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnLamMoi.setFocusTraversalKeysEnabled(false);
        btnLamMoi.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLamMoiActionPerformed(evt);
            }
        });

        btnFirst.setFont(new java.awt.Font("Open Sans", 0, 15)); // NOI18N
        btnFirst.setText("I<");
        btnFirst.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnFirst.setFocusTraversalKeysEnabled(false);
        btnFirst.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnFirstActionPerformed(evt);
            }
        });

        btnPrev.setFont(new java.awt.Font("Open Sans", 0, 15)); // NOI18N
        btnPrev.setText("<<");
        btnPrev.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnPrev.setFocusTraversalKeysEnabled(false);
        btnPrev.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPrevActionPerformed(evt);
            }
        });

        btnNext.setFont(new java.awt.Font("Open Sans", 0, 15)); // NOI18N
        btnNext.setText(">>");
        btnNext.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnNext.setFocusTraversalKeysEnabled(false);
        btnNext.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnNextActionPerformed(evt);
            }
        });

        btnLast.setFont(new java.awt.Font("Open Sans", 0, 15)); // NOI18N
        btnLast.setText(">I");
        btnLast.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnLast.setFocusTraversalKeysEnabled(false);
        btnLast.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLastActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(btnThem, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnCapNhat, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnXoa, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnLamMoi, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 141, Short.MAX_VALUE)
                .addComponent(btnFirst, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnPrev, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnNext, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnLast, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel7Layout.createSequentialGroup()
                .addGap(4, 4, 4)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnThem, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnCapNhat, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnXoa, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnLamMoi, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnLast, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnNext, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnPrev, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnFirst, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, 829, Short.MAX_VALUE)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, 372, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel7, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );

        panelTab.addTab("Thông Tin", jPanel1);

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 850, Short.MAX_VALUE)
            .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(panelTab, javax.swing.GroupLayout.Alignment.TRAILING))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 477, Short.MAX_VALUE)
            .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel4Layout.createSequentialGroup()
                    .addContainerGap()
                    .addComponent(panelTab)
                    .addContainerGap()))
        );

        jPanel6.setBackground(new java.awt.Color(255, 255, 255));

        jPanel8.setBackground(new java.awt.Color(255, 255, 255));
        jPanel8.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Tìm kiếm", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Open Sans", 0, 14))); // NOI18N

        txtTimKiem.setFont(new java.awt.Font("Open Sans", 0, 14)); // NOI18N
        txtTimKiem.setFocusTraversalKeysEnabled(false);
        txtTimKiem.setFocusable(false);
        txtTimKiem.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                txtTimKiemMouseClicked(evt);
            }
        });
        txtTimKiem.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtTimKiemKeyTyped(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtTimKiemKeyReleased(evt);
            }
        });

        cboBoLoc.setFont(new java.awt.Font("Open Sans", 0, 14)); // NOI18N
        cboBoLoc.setFocusTraversalKeysEnabled(false);
        cboBoLoc.setFocusable(false);
        cboBoLoc.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                cboBoLocPropertyChange(evt);
            }
        });

        jLabel3.setFont(new java.awt.Font("Open Sans", 0, 14)); // NOI18N
        jLabel3.setText("Thuộc tính:");

        jLabel11.setFont(new java.awt.Font("Open Sans", 0, 14)); // NOI18N
        jLabel11.setText("Nhập liệu:");

        javax.swing.GroupLayout jPanel8Layout = new javax.swing.GroupLayout(jPanel8);
        jPanel8.setLayout(jPanel8Layout);
        jPanel8Layout.setHorizontalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addGap(27, 27, 27)
                .addComponent(jLabel3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(cboBoLoc, javax.swing.GroupLayout.PREFERRED_SIZE, 280, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(50, 50, 50)
                .addComponent(jLabel11)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtTimKiem, javax.swing.GroupLayout.PREFERRED_SIZE, 274, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel8Layout.setVerticalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtTimKiem, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cboBoLoc, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel11, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(0, 9, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel8, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jPanel8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, 850, Short.MAX_VALUE)
            .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jPanel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(jPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnLamMoiActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLamMoiActionPerformed
        resetForm();
        setAddingState();
    }//GEN-LAST:event_btnLamMoiActionPerformed

    private void btnThemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnThemActionPerformed
        this.insertModel();
    }//GEN-LAST:event_btnThemActionPerformed

    private void btnXoaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnXoaActionPerformed
        this.deleteModel();
    }//GEN-LAST:event_btnXoaActionPerformed

    private void tblNhanVienMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblNhanVienMouseClicked
        selectedIndex = tblNhanVien.rowAtPoint(evt.getPoint());
        if (evt.getClickCount() == 2) {
            if (selectedIndex >= 0) {
                panelTab.setSelectedIndex(1);
            }
        }
    }//GEN-LAST:event_tblNhanVienMouseClicked

    private void btnCapNhatActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCapNhatActionPerformed
        this.updateModel();
    }//GEN-LAST:event_btnCapNhatActionPerformed

    private void btnFirstActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnFirstActionPerformed
        setEditingState();
        if (tableData.size() > 0) {
            selectedIndex = 0;
            changeSelectedIndex();
            btnNext.setEnabled(true);
        }
        btnFirst.setEnabled(false);
        btnLast.setEnabled(true);
        btnPrev.setEnabled(false);
    }//GEN-LAST:event_btnFirstActionPerformed

    private void btnLastActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLastActionPerformed
        setEditingState();
        if (tableData.size() > 0) {
            selectedIndex = tableData.size() - 1;
            changeSelectedIndex();
            btnPrev.setEnabled(true);
        }
        btnLast.setEnabled(false);
        btnFirst.setEnabled(true);
        btnNext.setEnabled(false);
    }//GEN-LAST:event_btnLastActionPerformed

    private void btnPrevActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPrevActionPerformed
        setEditingState();
        if (selectedIndex > 0) {
            selectedIndex--;
            changeSelectedIndex();
            btnNext.setEnabled(true);
            btnLast.setEnabled(true);
        } else {
            btnPrev.setEnabled(false);
            btnFirst.setEnabled(false);
        }
    }//GEN-LAST:event_btnPrevActionPerformed

    private void btnNextActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNextActionPerformed
        setEditingState();
        if (selectedIndex < tableData.size() - 1) {
            selectedIndex++;
            changeSelectedIndex();
            btnPrev.setEnabled(true);
            btnFirst.setEnabled(true);
        } else {
            btnNext.setEnabled(false);
            btnLast.setEnabled(false);
        }
    }//GEN-LAST:event_btnNextActionPerformed

    private void panelTabStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_panelTabStateChanged
//        selectedIndex = tblNhanVien.getSelectedRow();
        if (selectedIndex == -1) {
            setAddingState();
        } else if (panelTab.getSelectedIndex() == 1) {
            setEditingState();
            setModelToForm();
        }
    }//GEN-LAST:event_panelTabStateChanged

    private void formWindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosing
        tblNhanVien.clearSelection();
        selectedIndex = tblNhanVien.getSelectedRow();
        resetForm();
        setAddingState();
    }//GEN-LAST:event_formWindowClosing

    private void txtHoTenKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtHoTenKeyTyped
        if (String.valueOf(evt.getKeyChar()).matches("\\d")) {
            evt.consume();
        }
    }//GEN-LAST:event_txtHoTenKeyTyped

    private void txtSoDienThoaiKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtSoDienThoaiKeyTyped
        if (!String.valueOf(evt.getKeyChar()).matches("[\\d]")) {
            evt.consume();
        }
    }//GEN-LAST:event_txtSoDienThoaiKeyTyped

    private void txtTimKiemKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtTimKiemKeyTyped

    }//GEN-LAST:event_txtTimKiemKeyTyped

    private void txtTimKiemMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txtTimKiemMouseClicked
        txtTimKiem.setFocusable(true);
        txtTimKiem.requestFocus();
    }//GEN-LAST:event_txtTimKiemMouseClicked

    private void cboBoLocPropertyChange(java.beans.PropertyChangeEvent evt) {//GEN-FIRST:event_cboBoLocPropertyChange
        txtTimKiemKeyReleased(null);
    }//GEN-LAST:event_cboBoLocPropertyChange

    private void txtTimKiemKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtTimKiemKeyReleased
        if (isDataLoaded) {
            tableData.clear();
            int cboIndex = cboBoLoc.getSelectedIndex();
            String fieldName = ObjectStructureHelper.NHANVIEN_PROPERTIES[cboIndex];
            for (NhanVien nhanVien : nhanVienHashMap.values()) {
                Object dataFromField = DataFactoryUtil.getValueByField(nhanVien, fieldName);
//                Kiem tra co phai file vai tro hay khong
                if (dataFromField instanceof Boolean) {
                    dataFromField = ((Boolean) dataFromField).booleanValue() ? "Trưởng phòng" : "Nhân viên";
                }

                String strDataFromField = dataFromField.toString().toLowerCase();

                String timKiemString = txtTimKiem.getText().toLowerCase();
                if (strDataFromField.contains(timKiemString)) {
                    try {
                        addModelToTable(nhanVien);
                    } catch (Exception ex) {
                        Logger.getLogger(NhanVienJFrame.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
            tblNhanVien.updateUI();
        }
    }//GEN-LAST:event_txtTimKiemKeyReleased

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Windows".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(NhanVienJFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(NhanVienJFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(NhanVienJFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(NhanVienJFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new NhanVienJFrame().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnCapNhat;
    private javax.swing.JButton btnFirst;
    private javax.swing.JButton btnLamMoi;
    private javax.swing.JButton btnLast;
    private javax.swing.JButton btnNext;
    private javax.swing.JButton btnPrev;
    private javax.swing.JButton btnThem;
    private javax.swing.JButton btnXoa;
    private javax.swing.JComboBox<String> cboBoLoc;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTabbedPane panelTab;
    private javax.swing.JRadioButton rdoNam;
    private javax.swing.JRadioButton rdoNhanVien;
    private javax.swing.JRadioButton rdoNu;
    private javax.swing.JRadioButton rdoTruongPhong;
    private javax.swing.JTable tblNhanVien;
    private javax.swing.JTextField txtDiaChi;
    private javax.swing.JTextField txtEmail;
    private javax.swing.JTextField txtHoTen;
    private javax.swing.JTextField txtMaNhanVien;
    private javax.swing.JTextField txtNgaySinh;
    private javax.swing.JTextField txtSoDienThoai;
    private javax.swing.JTextField txtTimKiem;
    // End of variables declaration//GEN-END:variables
    private ButtonGroup gioiTinhGroup = new ButtonGroup();
    private ButtonGroup vaiTroGroup = new ButtonGroup();
}
