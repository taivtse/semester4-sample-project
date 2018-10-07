/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package poly.app.view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.io.File;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ButtonGroup;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import poly.app.core.constant.CoreConstant;
import poly.app.core.daoimpl.NguoiHocDaoImpl;
import poly.app.core.entities.NguoiHoc;
import poly.app.core.helper.DateHelper;
import poly.app.core.helper.ObjectStructureHelper;
import poly.app.core.helper.DialogHelper;
import poly.app.core.helper.URLHelper;
import poly.app.core.utils.DataFactoryUtil;
import poly.app.core.utils.EMailUtil;
import poly.app.core.utils.ImageUtil;
import poly.app.core.utils.StringUtil;

public class NguoiHocJFrame extends javax.swing.JFrame {

    Vector<Vector> tableData = new Vector<>();
    HashMap<String, NguoiHoc> nguoiHocHashMap = new HashMap<>();
    int selectedIndex = -1;
    boolean isDataLoaded = false;
    JFileChooser jFileChooser;

    public NguoiHocJFrame() {
        initComponents();
        setLocationRelativeTo(null);
        reRenderUI();
        addRadioToGroup();
        loadDataToTable();
    }

    private void reRenderUI() {
        DefaultTableModel tableModel = (DefaultTableModel) tblNguoiHoc.getModel();
        tableModel = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tableModel.setDataVector(tableData, new Vector(Arrays.asList(ObjectStructureHelper.NGUOIHOC_TABLE_IDENTIFIERS)));
        tblNguoiHoc.setModel(tableModel);

        JTableHeader jTableHeader = tblNguoiHoc.getTableHeader();
        jTableHeader.setFont(new Font("open sans", Font.PLAIN, 14)); // font name style size
        // canh giua man hinh
        ((DefaultTableCellRenderer) jTableHeader.getDefaultRenderer())
                .setHorizontalAlignment(JLabel.CENTER);
        // chieu cao header
        jTableHeader.setPreferredSize(new Dimension(0, 25));
        jTableHeader.setForeground(Color.decode("#000")); // change the Foreground
        tblNguoiHoc.setFillsViewportHeight(true);
        tblNguoiHoc.setBackground(Color.WHITE);

        DefaultTableCellRenderer cellLeft = new DefaultTableCellRenderer();
        cellLeft.setHorizontalAlignment(JLabel.LEFT);

        DefaultTableCellRenderer cellCenter = new DefaultTableCellRenderer();
        cellCenter.setHorizontalAlignment(JLabel.CENTER);

        DefaultTableCellRenderer cellRight = new DefaultTableCellRenderer();
        cellRight.setHorizontalAlignment(JLabel.RIGHT);

        tblNguoiHoc.getColumnModel().getColumn(0).setCellRenderer(cellLeft);
        tblNguoiHoc.getColumnModel().getColumn(1).setCellRenderer(cellLeft);
        tblNguoiHoc.getColumnModel().getColumn(2).setCellRenderer(cellRight);
        tblNguoiHoc.getColumnModel().getColumn(3).setCellRenderer(cellRight);
        tblNguoiHoc.getColumnModel().getColumn(4).setCellRenderer(cellCenter);

        // set width for column
        double tableWidth = tblNguoiHoc.getPreferredSize().getWidth();
        tblNguoiHoc.getColumnModel().getColumn(1).setPreferredWidth((int) (tableWidth * 0.35));
        tblNguoiHoc.getColumnModel().getColumn(3).setPreferredWidth((int) (tableWidth * 0.45));

//        Add data to combobox
        for (String identifier : ObjectStructureHelper.NGUOIHOC_TABLE_IDENTIFIERS) {
            cboBoLoc.addItem(identifier);
        }
        cboBoLoc.setSelectedIndex(1);

//        Add default image to user avatar
        lblAvatar.setIcon(ImageUtil.resizeImage(new File("./src/poly/app/view/icon/default-avatar.jpeg"), lblAvatar.getWidth(), lblAvatar.getHeight()));
    }

    private void addRadioToGroup() {
        gioiTinhGroup.add(rdoNam);
        gioiTinhGroup.add(rdoNu);
    }

    private void loadDataToTable() {
        tableData.clear();
        List<NguoiHoc> dataLoadedList = new NguoiHocDaoImpl().selectByProperties(null, null, "hoTen", CoreConstant.SORT_ASC, null, null);

        try {
            for (NguoiHoc nguoiHoc : dataLoadedList) {
//                Convert nguoihoc to vector
                Vector vData = DataFactoryUtil.objectToVectorByFields(nguoiHoc, ObjectStructureHelper.NGUOIHOC_PROPERTIES);
                tableData.add(vData);

//                Add nguoihoc to hashmap
                nguoiHocHashMap.put(nguoiHoc.getMaNh(), nguoiHoc);
            }
            tblNguoiHoc.updateUI();
        } catch (Exception ex) {
            Logger.getLogger(NguoiHocJFrame.class.getName()).log(Level.SEVERE, null, ex);
        }

        isDataLoaded = true;
    }

    private void changeSelectedIndex() {
        if (selectedIndex >= 0) {
            tblNguoiHoc.setRowSelectionInterval(selectedIndex, selectedIndex);
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
        String maNh = tblNguoiHoc.getValueAt(selectedIndex, 0).toString();
        NguoiHoc selectedNguoiHoc = nguoiHocHashMap.get(maNh);

        txtMaNguoiHoc.setText(selectedNguoiHoc.getMaNh());
        txtHoTen.setText(selectedNguoiHoc.getHoTen());
        txtNgaySinh.setText(DateHelper.toString(selectedNguoiHoc.getNgaySinh()));
        txtSoDienThoai.setText(selectedNguoiHoc.getDienThoai());
        txtEmail.setText(selectedNguoiHoc.getEmail());
        txtGhiChu.setText(selectedNguoiHoc.getGhiChu());

        File avatarFile = new File(URLHelper.URL_NGUOIHOC_IMAGE + selectedNguoiHoc.getHinh());
        if (!avatarFile.exists() || avatarFile.isDirectory()) {
            avatarFile = new File("./src/poly/app/view/icon/default-avatar.jpeg");
        }
        lblAvatar.setIcon(ImageUtil.resizeImage(avatarFile, lblAvatar.getWidth(), lblAvatar.getHeight()));

        if (selectedNguoiHoc.getGioiTinh()) {
            rdoNam.setSelected(true);
        } else {
            rdoNu.setSelected(true);
        }
    }

    private void resetForm() {
        txtMaNguoiHoc.setText("");
        txtHoTen.setText("");
        txtNgaySinh.setText("");
        txtSoDienThoai.setText("");
        txtEmail.setText("");
        txtGhiChu.setText("");
        lblAvatar.setIcon(ImageUtil.resizeImage(new File("./src/poly/app/view/icon/default-avatar.jpeg"), lblAvatar.getWidth(), lblAvatar.getHeight()));
        txtMaNguoiHoc.requestFocus();

        rdoNam.setSelected(true);
    }

    private void addModelToTable(NguoiHoc nguoiHoc) {
        Vector vData = new Vector();
        vData.add(nguoiHoc.getMaNh());
        vData.add(nguoiHoc.getHoTen());
        vData.add(nguoiHoc.getDienThoai());
        vData.add(nguoiHoc.getEmail());
        vData.add(nguoiHoc.getNgayDk());

        tableData.add(vData);
    }

    private NguoiHoc getModelFromForm() {
        NguoiHoc nguoiHoc = new NguoiHoc();

        nguoiHoc.setMaNh(txtMaNguoiHoc.getText());
        nguoiHoc.setHoTen(txtHoTen.getText());
        nguoiHoc.setNgaySinh(DateHelper.toDate(txtNgaySinh.getText()));
        nguoiHoc.setGioiTinh(rdoNam.isSelected());
        nguoiHoc.setDienThoai(txtSoDienThoai.getText());
        nguoiHoc.setEmail(txtEmail.getText());
        nguoiHoc.setGhiChu(txtGhiChu.getText());

        return nguoiHoc;
    }

    private boolean validateInputAddingState() {
        for (NguoiHoc nguoiHoc : nguoiHocHashMap.values()) {
            if (nguoiHoc.getMaNh().equals(txtMaNguoiHoc.getText())) {
                DialogHelper.message(this, "Mã nhân viên đã tồn tại", DialogHelper.ERROR_MESSAGE);
                return false;
            } else if (nguoiHoc.getEmail().equals(txtEmail.getText())) {
                DialogHelper.message(this, "Email đã tồn tại", DialogHelper.ERROR_MESSAGE);
                return false;
            }
        }

//        TODO: validate here
        return true;
    }

    private boolean validateInputEditingState() {
        String maNh = tblNguoiHoc.getValueAt(selectedIndex, 0).toString();
        if (!nguoiHocHashMap.get(maNh).getEmail().equals(txtEmail.getText())) {
            for (NguoiHoc nguoiHoc : nguoiHocHashMap.values()) {
                if (nguoiHoc.getEmail().equals(txtEmail.getText())) {
                    DialogHelper.message(this, "Email đã tồn tại", DialogHelper.ERROR_MESSAGE);
                    return false;
                }
            }
        }

//        TODO: validate here
        return true;
    }

    private void setAddingState() {
        txtMaNguoiHoc.setEditable(true);
        txtMaNguoiHoc.requestFocus();
        btnThem.setEnabled(true);
        btnLamMoi.setEnabled(true);
        btnCapNhat.setEnabled(false);
        btnXoa.setEnabled(false);

        btnLast.setEnabled(false);
        btnPrev.setEnabled(false);
        btnFirst.setEnabled(false);
        btnNext.setEnabled(false);
    }

    private void setEditingState() {
        txtMaNguoiHoc.setEditable(false);
        btnThem.setEnabled(false);
        btnLamMoi.setEnabled(true);
        btnCapNhat.setEnabled(true);
        btnXoa.setEnabled(true);
    }

    private void insertModel() {
        if (validateInputAddingState()) {
            NguoiHoc nguoiHoc = getModelFromForm();

//            Save image to folder
            try {
                if (jFileChooser.getSelectedFile() != null) {
                    nguoiHoc.setHinh(nguoiHoc.getMaNh().concat(".jpg"));

                    if (nguoiHoc.getHinh() != null) {
                        ImageUtil.deleteImage(URLHelper.URL_NGUOIHOC_IMAGE, nguoiHoc.getHinh());
                    }
                    ImageUtil.saveImage(URLHelper.URL_NGUOIHOC_IMAGE, nguoiHoc.getHinh(), jFileChooser.getSelectedFile());
                    jFileChooser = null;
                }
            } catch (Exception ex) {
//                ex.printStackTrace();
            }

            boolean isInserted = new NguoiHocDaoImpl().insert(nguoiHoc);

            if (isInserted) {
                DialogHelper.message(this, "Thêm dữ liệu thành công.", DialogHelper.INFORMATION_MESSAGE);
                loadDataToTable();
                tblNguoiHoc.updateUI();
                setEditingState();
            } else {
                DialogHelper.message(this, "Thêm dữ liệu thất bại.", DialogHelper.ERROR_MESSAGE);
            }
        }
    }

    private void deleteModel() {
        boolean isConfirm = DialogHelper.confirm(this, "Bạn chắc chắn muốn xoá?");
        if (isConfirm) {
            String maNh = tblNguoiHoc.getValueAt(selectedIndex, 0).toString();
            NguoiHoc nguoiHoc = nguoiHocHashMap.get(maNh);
            boolean isDeleted = new NguoiHocDaoImpl().delete(nguoiHoc);

            if (isDeleted) {
                DialogHelper.message(this, "Xoá dữ liệu thành công.", DialogHelper.INFORMATION_MESSAGE);
                loadDataToTable();
                tblNguoiHoc.updateUI();
                changeSelectedIndex();
            } else {
                DialogHelper.message(this, "Xoá dữ liệu thất bại.", DialogHelper.ERROR_MESSAGE);
            }
        }
    }

    private void updateModel() {
        if (validateInputEditingState()) {
            String maNh = tblNguoiHoc.getValueAt(selectedIndex, 0).toString();
            NguoiHoc nguoiHocOldData = nguoiHocHashMap.get(maNh);

            NguoiHoc nguoiHocNewData = getModelFromForm();

//            Save image to folder
            try {
                if (jFileChooser.getSelectedFile() != null) {
                    nguoiHocNewData.setHinh(nguoiHocNewData.getMaNh().concat(".jpg"));

                    if (nguoiHocOldData.getHinh() != null) {
                        ImageUtil.deleteImage(URLHelper.URL_NGUOIHOC_IMAGE, nguoiHocOldData.getHinh());
                    }
                    ImageUtil.saveImage(URLHelper.URL_NGUOIHOC_IMAGE, nguoiHocNewData.getHinh(), jFileChooser.getSelectedFile());
                    jFileChooser = null;
                }
            } catch (Exception ex) {
//                ex.printStackTrace();
            }

            boolean isUpdated = false;
            try {
                nguoiHocNewData = DataFactoryUtil.mergeTwoObject(nguoiHocOldData, nguoiHocNewData);
                nguoiHocHashMap.put(maNh, nguoiHocNewData);

                isUpdated = new NguoiHocDaoImpl().update(nguoiHocNewData);
            } catch (Exception ex) {
                Logger.getLogger(NguoiHocJFrame.class.getName()).log(Level.SEVERE, null, ex);
            }

            if (isUpdated) {
                Vector vData;
                try {
                    vData = DataFactoryUtil.objectToVectorByFields(nguoiHocNewData, ObjectStructureHelper.NGUOIHOC_PROPERTIES);

//                    Find index of updated NguoiHoc in tabledata
                    for (int i = 0; i < tableData.size(); i++) {
                        if (tableData.get(i).get(0).equals(vData.get(0))) {
                            tableData.set(i, vData);
                            break;
                        }
                    }

                    tblNguoiHoc.updateUI();
                    DialogHelper.message(this, "Cập nhật dữ liệu thành công.", DialogHelper.INFORMATION_MESSAGE);
                } catch (Exception ex) {
                    Logger.getLogger(NguoiHocJFrame.class.getName()).log(Level.SEVERE, null, ex);
                }
            } else {
                DialogHelper.message(this, "Cập nhật dữ liệu thất bại.", DialogHelper.ERROR_MESSAGE);
            }
        }
    }

    private void setDirectionButton() {
        if (tableData.size() > 0 || selectedIndex != -1) {
            if (selectedIndex == 0) {
                btnFirst.setEnabled(false);
                btnPrev.setEnabled(false);
            }

            if (selectedIndex > 0) {
                btnFirst.setEnabled(true);
                btnPrev.setEnabled(true);
            }

            if (selectedIndex == tableData.size() - 1) {
                btnLast.setEnabled(false);
                btnNext.setEnabled(false);
            }

            if (selectedIndex < tableData.size() - 1) {
                btnLast.setEnabled(true);
                btnNext.setEnabled(true);
            }
        } else {
            btnFirst.setEnabled(false);
            btnPrev.setEnabled(false);
            btnNext.setEnabled(false);
            btnLast.setEnabled(false);
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
        tblNguoiHoc = new javax.swing.JTable();
        jPanel1 = new javax.swing.JPanel();
        jPanel5 = new javax.swing.JPanel();
        txtMaNguoiHoc = new javax.swing.JTextField();
        txtHoTen = new javax.swing.JTextField();
        rdoNam = new javax.swing.JRadioButton();
        rdoNu = new javax.swing.JRadioButton();
        txtNgaySinh = new javax.swing.JTextField();
        txtSoDienThoai = new javax.swing.JTextField();
        txtEmail = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        lblAvatar = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        txtGhiChu = new javax.swing.JTextArea();
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
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                formWindowClosing(evt);
            }
            public void windowDeactivated(java.awt.event.WindowEvent evt) {
                formWindowDeactivated(evt);
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

        tblNguoiHoc.setAutoCreateRowSorter(true);
        tblNguoiHoc.setFont(new java.awt.Font("Open Sans", 0, 14)); // NOI18N
        tblNguoiHoc.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {

            }
        ));
        tblNguoiHoc.setGridColor(new java.awt.Color(204, 204, 204));
        tblNguoiHoc.setIntercellSpacing(new java.awt.Dimension(0, 1));
        tblNguoiHoc.setRowHeight(23);
        tblNguoiHoc.setSelectionBackground(new java.awt.Color(65, 76, 89));
        tblNguoiHoc.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        tblNguoiHoc.setShowGrid(false);
        tblNguoiHoc.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblNguoiHocMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(tblNguoiHoc);

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 829, Short.MAX_VALUE)
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 440, Short.MAX_VALUE)
        );

        panelTab.addTab("Danh Sách", jPanel2);

        jPanel5.setFocusTraversalKeysEnabled(false);
        jPanel5.setFocusable(false);
        jPanel5.setPreferredSize(new java.awt.Dimension(408, 390));

        txtMaNguoiHoc.setEditable(false);
        txtMaNguoiHoc.setFont(new java.awt.Font("Open Sans", 0, 14)); // NOI18N
        txtMaNguoiHoc.setFocusTraversalKeysEnabled(false);

        txtHoTen.setFont(new java.awt.Font("Open Sans", 0, 14)); // NOI18N
        txtHoTen.setFocusTraversalKeysEnabled(false);
        txtHoTen.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtHoTenKeyTyped(evt);
            }
        });

        rdoNam.setFont(new java.awt.Font("Open Sans", 0, 14)); // NOI18N
        rdoNam.setSelected(true);
        rdoNam.setText("Nam");
        rdoNam.setFocusTraversalKeysEnabled(false);

        rdoNu.setFont(new java.awt.Font("Open Sans", 0, 14)); // NOI18N
        rdoNu.setText("Nữ");
        rdoNu.setFocusTraversalKeysEnabled(false);

        txtNgaySinh.setFont(new java.awt.Font("Open Sans", 0, 14)); // NOI18N
        txtNgaySinh.setFocusTraversalKeysEnabled(false);

        txtSoDienThoai.setFont(new java.awt.Font("Open Sans", 0, 14)); // NOI18N
        txtSoDienThoai.setFocusTraversalKeysEnabled(false);
        txtSoDienThoai.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtSoDienThoaiKeyTyped(evt);
            }
        });

        txtEmail.setFont(new java.awt.Font("Open Sans", 0, 14)); // NOI18N
        txtEmail.setFocusTraversalKeysEnabled(false);

        jLabel7.setFont(new java.awt.Font("Open Sans", 0, 14)); // NOI18N
        jLabel7.setText("Ngày sinh");

        jLabel5.setFont(new java.awt.Font("Open Sans", 0, 14)); // NOI18N
        jLabel5.setText("Họ  và tên");

        jLabel2.setFont(new java.awt.Font("Open Sans", 0, 14)); // NOI18N
        jLabel2.setText("Mã người học");

        jLabel10.setFont(new java.awt.Font("Open Sans", 0, 14)); // NOI18N
        jLabel10.setText("Giới tính");

        jLabel4.setFont(new java.awt.Font("Open Sans", 0, 14)); // NOI18N
        jLabel4.setText("Số điện thoại");

        jLabel8.setFont(new java.awt.Font("Open Sans", 0, 14)); // NOI18N
        jLabel8.setText("Email");

        jLabel9.setFont(new java.awt.Font("Open Sans", 0, 14)); // NOI18N
        jLabel9.setText("Ghi chú");

        lblAvatar.setBackground(new java.awt.Color(255, 255, 255));
        lblAvatar.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        lblAvatar.setOpaque(true);
        lblAvatar.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lblAvatarMouseClicked(evt);
            }
        });

        txtGhiChu.setColumns(20);
        txtGhiChu.setRows(5);
        jScrollPane2.setViewportView(txtGhiChu);

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addGap(31, 31, 31)
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jLabel5)
                            .addComponent(txtMaNguoiHoc)
                            .addComponent(jLabel2)
                            .addComponent(txtHoTen, javax.swing.GroupLayout.PREFERRED_SIZE, 360, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanel5Layout.createSequentialGroup()
                                .addGap(110, 110, 110)
                                .addComponent(rdoNu))
                            .addComponent(rdoNam)
                            .addComponent(jLabel10)))
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addGap(138, 138, 138)
                        .addComponent(lblAvatar, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 47, Short.MAX_VALUE)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel4)
                    .addComponent(jLabel9)
                    .addComponent(txtSoDienThoai, javax.swing.GroupLayout.DEFAULT_SIZE, 360, Short.MAX_VALUE)
                    .addComponent(txtEmail, javax.swing.GroupLayout.DEFAULT_SIZE, 360, Short.MAX_VALUE)
                    .addComponent(jLabel8)
                    .addComponent(jLabel7)
                    .addComponent(txtNgaySinh, javax.swing.GroupLayout.DEFAULT_SIZE, 360, Short.MAX_VALUE)
                    .addComponent(jScrollPane2))
                .addGap(31, 31, 31))
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtNgaySinh, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtSoDienThoai, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addComponent(lblAvatar, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel5Layout.createSequentialGroup()
                        .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtEmail, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel9, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jScrollPane2))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel5Layout.createSequentialGroup()
                        .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtMaNguoiHoc, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtHoTen, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel10, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(rdoNam)
                            .addComponent(rdoNu))))
                .addGap(15, 15, 15))
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
                .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, 389, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 10, Short.MAX_VALUE)
                .addComponent(jPanel7, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE))
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
            .addGap(0, 498, Short.MAX_VALUE)
            .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel4Layout.createSequentialGroup()
                    .addContainerGap()
                    .addComponent(panelTab, javax.swing.GroupLayout.DEFAULT_SIZE, 486, Short.MAX_VALUE)
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
                .addGap(0, 0, 0)
                .addComponent(jPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnLamMoiActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLamMoiActionPerformed
        selectedIndex = -1;
        tblNguoiHoc.clearSelection();
        resetForm();
        setAddingState();
    }//GEN-LAST:event_btnLamMoiActionPerformed

    private void btnThemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnThemActionPerformed
        this.insertModel();
    }//GEN-LAST:event_btnThemActionPerformed

    private void btnXoaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnXoaActionPerformed
        this.deleteModel();
    }//GEN-LAST:event_btnXoaActionPerformed

    private void tblNguoiHocMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblNguoiHocMouseClicked
        selectedIndex = tblNguoiHoc.rowAtPoint(evt.getPoint());
        if (evt.getClickCount() == 2) {
            if (selectedIndex >= 0) {
                panelTab.setSelectedIndex(1);
                setDirectionButton();
            }
        }
    }//GEN-LAST:event_tblNguoiHocMouseClicked

    private void btnCapNhatActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCapNhatActionPerformed
        this.updateModel();
    }//GEN-LAST:event_btnCapNhatActionPerformed

    private void btnFirstActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnFirstActionPerformed
        setEditingState();
        if (tableData.size() > 0) {
            selectedIndex = 0;
            changeSelectedIndex();
        }
        setDirectionButton();
    }//GEN-LAST:event_btnFirstActionPerformed

    private void btnLastActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLastActionPerformed
        setEditingState();
        if (tableData.size() > 0) {
            selectedIndex = tableData.size() - 1;
            changeSelectedIndex();
        }

        setDirectionButton();
    }//GEN-LAST:event_btnLastActionPerformed

    private void btnPrevActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPrevActionPerformed
        setEditingState();
        if (selectedIndex > 0) {
            selectedIndex--;
            changeSelectedIndex();
        }

        setDirectionButton();
    }//GEN-LAST:event_btnPrevActionPerformed

    private void btnNextActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNextActionPerformed
        setEditingState();
        if (selectedIndex < tableData.size() - 1) {
            selectedIndex++;
            changeSelectedIndex();
        }

        setDirectionButton();
    }//GEN-LAST:event_btnNextActionPerformed

    private void panelTabStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_panelTabStateChanged
        if (evt != null && isDataLoaded) {
            selectedIndex = tblNguoiHoc.getSelectedRow();
            if (selectedIndex == -1) {
                resetForm();
                setAddingState();
            } else if (panelTab.getSelectedIndex() == 1) {
                setEditingState();
                setModelToForm();
                txtTimKiem.setFocusable(false);
                requestFocusInWindow();
            }
        }
    }//GEN-LAST:event_panelTabStateChanged

    private void formWindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosing
        tblNguoiHoc.clearSelection();
        selectedIndex = tblNguoiHoc.getSelectedRow();
        resetForm();
        setAddingState();
        panelTab.setSelectedIndex(0);
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
        selectedIndex = -1;
        tblNguoiHoc.clearSelection();
        txtTimKiem.setFocusable(true);
        txtTimKiem.requestFocus();
        panelTab.setSelectedIndex(0);
    }//GEN-LAST:event_txtTimKiemMouseClicked

    private void cboBoLocPropertyChange(java.beans.PropertyChangeEvent evt) {//GEN-FIRST:event_cboBoLocPropertyChange
        txtTimKiemKeyReleased(null);
    }//GEN-LAST:event_cboBoLocPropertyChange

    private void txtTimKiemKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtTimKiemKeyReleased
        if (evt != null && isDataLoaded) {
            tableData.clear();
            int cboIndex = cboBoLoc.getSelectedIndex();
            String fieldName = ObjectStructureHelper.NHANVIEN_PROPERTIES[cboIndex];
            for (NguoiHoc nguoiHoc : nguoiHocHashMap.values()) {
                Object dataFromField = DataFactoryUtil.getValueByField(nguoiHoc, fieldName);
//                Kiem tra co phai file vai tro hay khong
                if (dataFromField instanceof Boolean) {
                    dataFromField = ((Boolean) dataFromField).booleanValue() ? "Trưởng phòng" : "Nhân viên";
                }

                String strDataFromField = dataFromField.toString().toLowerCase();

                String timKiemString = txtTimKiem.getText().toLowerCase();
                if (strDataFromField.contains(timKiemString)) {
                    try {
                        addModelToTable(nguoiHoc);

                    } catch (Exception ex) {
                        Logger.getLogger(NguoiHocJFrame.class
                                .getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
            tblNguoiHoc.updateUI();
        }
    }//GEN-LAST:event_txtTimKiemKeyReleased

    private void lblAvatarMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblAvatarMouseClicked
        jFileChooser = new JFileChooser();
        FileNameExtensionFilter filter = new FileNameExtensionFilter("Images", "jpeg", "jpg", "png", "gif");
        jFileChooser.setFileFilter(filter);
        int result = jFileChooser.showOpenDialog(this);
        if (result == JFileChooser.APPROVE_OPTION) {
            try {
                lblAvatar.setIcon(ImageUtil.resizeImage(jFileChooser.getSelectedFile(), lblAvatar.getWidth(), lblAvatar.getHeight()));
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }//GEN-LAST:event_lblAvatarMouseClicked

    private void formWindowDeactivated(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowDeactivated
        txtTimKiem.setRequestFocusEnabled(false);
    }//GEN-LAST:event_formWindowDeactivated

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
            java.util.logging.Logger.getLogger(NguoiHocJFrame.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);

        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(NguoiHocJFrame.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);

        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(NguoiHocJFrame.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);

        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(NguoiHocJFrame.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new NguoiHocJFrame().setVisible(true);
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
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JLabel lblAvatar;
    private javax.swing.JTabbedPane panelTab;
    private javax.swing.JRadioButton rdoNam;
    private javax.swing.JRadioButton rdoNu;
    private javax.swing.JTable tblNguoiHoc;
    private javax.swing.JTextField txtEmail;
    private javax.swing.JTextArea txtGhiChu;
    private javax.swing.JTextField txtHoTen;
    private javax.swing.JTextField txtMaNguoiHoc;
    private javax.swing.JTextField txtNgaySinh;
    private javax.swing.JTextField txtSoDienThoai;
    private javax.swing.JTextField txtTimKiem;
    // End of variables declaration//GEN-END:variables
    private ButtonGroup gioiTinhGroup = new ButtonGroup();
}
