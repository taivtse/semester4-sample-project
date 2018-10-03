/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package poly.app.view;

import com.apple.eawt.Application;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.Toolkit;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import javax.swing.Timer;
import poly.app.core.daoimpl.NhanVienDaoImpl;
import poly.app.core.entities.NhanVien;
import poly.app.core.helper.DialogHelper;
import poly.app.core.helper.ShareHelper;
import poly.app.core.utils.HibernateUtil;

/**
 *
 * @author vothanhtai
 */
public class MainJFrame extends javax.swing.JFrame {

    NhanVienJFrame nhanVienJFrame;

    /**
     * Creates new form MainJFrame
     */
    public MainJFrame() {
        changeAppIcon();
        initComponents();
        loadSessionFactory();
        init();
    }

    private void changeAppIcon() {
        setIconImage(ShareHelper.APP_ICON);
        Application.getApplication().setDockIconImage(ShareHelper.APP_ICON);
    }

    private void loadSessionFactory() {
        new Thread(() -> {
            HibernateUtil.getSessionFactory();
            nhanVienJFrame = new NhanVienJFrame();
        }).start();
    }

    public void init() {
        this.setAutoRequestFocus(true);
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        this.setSize(screenSize);
        new Timer(1000, new ActionListener() {
            SimpleDateFormat sdf = new SimpleDateFormat("hh:mm:ss a");

            @Override
            public void actionPerformed(ActionEvent e) {
                lblDongHo.setText(sdf.format(new Date()));
            }
        }).start();

        openChaoDialog();
        openDangNhapDialog();
    }

    private void openChaoDialog() {
        new ChaoJDialog(this, true).setVisible(true);
    }

    private void openDangNhapDialog() {
        new DangNhapJDialog(this, true).setVisible(true);
    }

    private void openDoiMatKhauDialog() {
        new DoiMatKhauJDialog(this, true).setVisible(true);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jMenu1 = new javax.swing.JMenu();
        jSeparator7 = new javax.swing.JSeparator();
        jSeparator10 = new javax.swing.JSeparator();
        jSeparator11 = new javax.swing.JSeparator();
        jPanel1 = new javax.swing.JPanel();
        jToolBar1 = new javax.swing.JToolBar();
        btnDangXuat = new javax.swing.JButton();
        btnKetThuc = new javax.swing.JButton();
        jSeparator9 = new javax.swing.JToolBar.Separator();
        btnNguoiHoc = new javax.swing.JButton();
        btnChuyenDe = new javax.swing.JButton();
        btnKhoaHoc = new javax.swing.JButton();
        btnNhanVien = new javax.swing.JButton();
        jSeparator6 = new javax.swing.JToolBar.Separator();
        btnHuongDan = new javax.swing.JButton();
        jSeparator12 = new javax.swing.JToolBar.Separator();
        jPanel2 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        lblThongTin = new javax.swing.JLabel();
        jSeparator8 = new javax.swing.JSeparator();
        lblDongHo = new javax.swing.JLabel();
        jMenuBar1 = new javax.swing.JMenuBar();
        jMenu2 = new javax.swing.JMenu();
        mniDangNhap = new javax.swing.JMenuItem();
        mniLDangXuat = new javax.swing.JMenuItem();
        jSeparator1 = new javax.swing.JPopupMenu.Separator();
        mniDoiMatKhau = new javax.swing.JMenuItem();
        jSeparator2 = new javax.swing.JPopupMenu.Separator();
        mniKetThuc = new javax.swing.JMenuItem();
        jMenu3 = new javax.swing.JMenu();
        mniQLNguoiHoc = new javax.swing.JMenuItem();
        mniQLChuyenDe = new javax.swing.JMenuItem();
        mniQLKhoaHoc = new javax.swing.JMenuItem();
        mniQLNhanVien = new javax.swing.JMenuItem();
        mniNguoiHocTungNam = new javax.swing.JMenu();
        mniTKNguoiHoc = new javax.swing.JMenuItem();
        jSeparator3 = new javax.swing.JPopupMenu.Separator();
        mniTKBangDiemKhoa = new javax.swing.JMenuItem();
        mniTKDiemTungKhoaHoc = new javax.swing.JMenuItem();
        jSeparator4 = new javax.swing.JPopupMenu.Separator();
        mniTKDoanhThu = new javax.swing.JMenuItem();
        jMenu5 = new javax.swing.JMenu();
        mniHuongDanSuDung = new javax.swing.JMenuItem();
        jSeparator5 = new javax.swing.JPopupMenu.Separator();
        mniGioiThieuSanPham = new javax.swing.JMenuItem();

        jMenu1.setText("jMenu1");

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setAutoRequestFocus(false);
        setFocusTraversalPolicyProvider(true);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowActivated(java.awt.event.WindowEvent evt) {
                formWindowActivated(evt);
            }
            public void windowDeactivated(java.awt.event.WindowEvent evt) {
                formWindowDeactivated(evt);
            }
        });

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));
        jPanel1.setPreferredSize(new java.awt.Dimension(369, 178));

        jToolBar1.setBackground(new java.awt.Color(65, 76, 89));
        jToolBar1.setRollover(true);

        btnDangXuat.setFont(new java.awt.Font("Open Sans", 0, 14)); // NOI18N
        btnDangXuat.setForeground(new java.awt.Color(252, 252, 252));
        btnDangXuat.setIcon(new javax.swing.ImageIcon(getClass().getResource("/poly/app/view/icon/Log out.png"))); // NOI18N
        btnDangXuat.setText("Đăng xuất");
        btnDangXuat.setBorder(javax.swing.BorderFactory.createEmptyBorder(15, 10, 15, 10));
        btnDangXuat.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnDangXuat.setFocusable(false);
        btnDangXuat.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnDangXuat.setMargin(new java.awt.Insets(0, 20, 0, 20));
        btnDangXuat.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnDangXuat.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDangXuatActionPerformed(evt);
            }
        });
        jToolBar1.add(btnDangXuat);

        btnKetThuc.setFont(new java.awt.Font("Open Sans", 0, 14)); // NOI18N
        btnKetThuc.setForeground(new java.awt.Color(252, 252, 252));
        btnKetThuc.setIcon(new javax.swing.ImageIcon(getClass().getResource("/poly/app/view/icon/Stop.png"))); // NOI18N
        btnKetThuc.setText("Kết thúc");
        btnKetThuc.setBorder(javax.swing.BorderFactory.createEmptyBorder(15, 10, 15, 10));
        btnKetThuc.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnKetThuc.setFocusable(false);
        btnKetThuc.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnKetThuc.setMargin(new java.awt.Insets(0, 20, 0, 20));
        btnKetThuc.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnKetThuc.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnKetThucActionPerformed(evt);
            }
        });
        jToolBar1.add(btnKetThuc);
        jToolBar1.add(jSeparator9);

        btnNguoiHoc.setFont(new java.awt.Font("Open Sans", 0, 14)); // NOI18N
        btnNguoiHoc.setForeground(new java.awt.Color(252, 252, 252));
        btnNguoiHoc.setIcon(new javax.swing.ImageIcon(getClass().getResource("/poly/app/view/icon/Conference.png"))); // NOI18N
        btnNguoiHoc.setText("Người học");
        btnNguoiHoc.setBorder(javax.swing.BorderFactory.createEmptyBorder(15, 10, 15, 10));
        btnNguoiHoc.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnNguoiHoc.setFocusable(false);
        btnNguoiHoc.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnNguoiHoc.setMargin(new java.awt.Insets(0, 20, 0, 20));
        btnNguoiHoc.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnNguoiHoc.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnNguoiHocActionPerformed(evt);
            }
        });
        jToolBar1.add(btnNguoiHoc);

        btnChuyenDe.setFont(new java.awt.Font("Open Sans", 0, 14)); // NOI18N
        btnChuyenDe.setForeground(new java.awt.Color(252, 252, 252));
        btnChuyenDe.setIcon(new javax.swing.ImageIcon(getClass().getResource("/poly/app/view/icon/Lists.png"))); // NOI18N
        btnChuyenDe.setText("Chuyên đề");
        btnChuyenDe.setBorder(javax.swing.BorderFactory.createEmptyBorder(15, 10, 15, 10));
        btnChuyenDe.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnChuyenDe.setFocusable(false);
        btnChuyenDe.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnChuyenDe.setMargin(new java.awt.Insets(0, 20, 0, 20));
        btnChuyenDe.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnChuyenDe.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnChuyenDeActionPerformed(evt);
            }
        });
        jToolBar1.add(btnChuyenDe);

        btnKhoaHoc.setFont(new java.awt.Font("Open Sans", 0, 14)); // NOI18N
        btnKhoaHoc.setForeground(new java.awt.Color(252, 252, 252));
        btnKhoaHoc.setIcon(new javax.swing.ImageIcon(getClass().getResource("/poly/app/view/icon/Certificate.png"))); // NOI18N
        btnKhoaHoc.setText("Khoá học");
        btnKhoaHoc.setBorder(javax.swing.BorderFactory.createEmptyBorder(15, 10, 15, 10));
        btnKhoaHoc.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnKhoaHoc.setFocusable(false);
        btnKhoaHoc.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnKhoaHoc.setMargin(new java.awt.Insets(0, 20, 0, 20));
        btnKhoaHoc.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnKhoaHoc.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnKhoaHocActionPerformed(evt);
            }
        });
        jToolBar1.add(btnKhoaHoc);

        btnNhanVien.setFont(new java.awt.Font("Open Sans", 0, 14)); // NOI18N
        btnNhanVien.setForeground(new java.awt.Color(252, 252, 252));
        btnNhanVien.setIcon(new javax.swing.ImageIcon(getClass().getResource("/poly/app/view/icon/User group.png"))); // NOI18N
        btnNhanVien.setText("Nhân viên");
        btnNhanVien.setBorder(javax.swing.BorderFactory.createEmptyBorder(15, 10, 15, 10));
        btnNhanVien.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnNhanVien.setFocusable(false);
        btnNhanVien.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnNhanVien.setMargin(new java.awt.Insets(0, 20, 0, 20));
        btnNhanVien.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnNhanVien.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnNhanVienActionPerformed(evt);
            }
        });
        jToolBar1.add(btnNhanVien);
        jToolBar1.add(jSeparator6);

        btnHuongDan.setBackground(new java.awt.Color(102, 102, 0));
        btnHuongDan.setFont(new java.awt.Font("Open Sans", 0, 14)); // NOI18N
        btnHuongDan.setForeground(new java.awt.Color(252, 252, 252));
        btnHuongDan.setIcon(new javax.swing.ImageIcon(getClass().getResource("/poly/app/view/icon/Globe.png"))); // NOI18N
        btnHuongDan.setText("Hướng dẫn");
        btnHuongDan.setBorder(javax.swing.BorderFactory.createEmptyBorder(15, 10, 15, 10));
        btnHuongDan.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnHuongDan.setFocusable(false);
        btnHuongDan.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnHuongDan.setMargin(new java.awt.Insets(0, 20, 0, 20));
        btnHuongDan.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jToolBar1.add(btnHuongDan);
        jToolBar1.add(jSeparator12);

        jPanel2.setBackground(new java.awt.Color(255, 255, 255));
        jPanel2.setOpaque(false);

        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/poly/app/view/icon/logo.png"))); // NOI18N
        jLabel1.setBorder(javax.swing.BorderFactory.createEmptyBorder(20, 1, 1, 1));
        jPanel2.add(jLabel1);

        jPanel3.setOpaque(false);

        lblThongTin.setFont(new java.awt.Font("Open Sans", 0, 14)); // NOI18N
        lblThongTin.setIcon(new javax.swing.ImageIcon(getClass().getResource("/poly/app/view/icon/Info.png"))); // NOI18N
        lblThongTin.setText("Hệ thống quản lý đào tạo");
        lblThongTin.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));

        jSeparator8.setForeground(new java.awt.Color(65, 76, 89));

        lblDongHo.setFont(new java.awt.Font("Open Sans", 0, 14)); // NOI18N
        lblDongHo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/poly/app/view/icon/Alarm.png"))); // NOI18N

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblThongTin)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(lblDongHo, javax.swing.GroupLayout.PREFERRED_SIZE, 121, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
            .addComponent(jSeparator8)
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addComponent(jSeparator8, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblThongTin, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblDongHo, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jToolBar1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, 1200, Short.MAX_VALUE)
            .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jToolBar1, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, 569, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        jMenuBar1.setBorder(null);

        jMenu2.setText("Hệ thống");

        mniDangNhap.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_L, java.awt.event.InputEvent.CTRL_MASK));
        mniDangNhap.setFont(new java.awt.Font("Open Sans", 0, 14)); // NOI18N
        mniDangNhap.setIcon(new javax.swing.ImageIcon(getClass().getResource("/poly/app/view/icon/Key.png"))); // NOI18N
        mniDangNhap.setText("Đăng nhập");
        mniDangNhap.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mniDangNhapActionPerformed(evt);
            }
        });
        jMenu2.add(mniDangNhap);

        mniLDangXuat.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_O, java.awt.event.InputEvent.CTRL_MASK));
        mniLDangXuat.setFont(new java.awt.Font("Open Sans", 0, 14)); // NOI18N
        mniLDangXuat.setIcon(new javax.swing.ImageIcon(getClass().getResource("/poly/app/view/icon/Log out.png"))); // NOI18N
        mniLDangXuat.setText("Đăng xuất");
        mniLDangXuat.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mniLDangXuatActionPerformed(evt);
            }
        });
        jMenu2.add(mniLDangXuat);
        jMenu2.add(jSeparator1);

        mniDoiMatKhau.setFont(new java.awt.Font("Open Sans", 0, 14)); // NOI18N
        mniDoiMatKhau.setIcon(new javax.swing.ImageIcon(getClass().getResource("/poly/app/view/icon/Refresh.png"))); // NOI18N
        mniDoiMatKhau.setText("Đổi mật khẩu");
        mniDoiMatKhau.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mniDoiMatKhauActionPerformed(evt);
            }
        });
        jMenu2.add(mniDoiMatKhau);
        jMenu2.add(jSeparator2);

        mniKetThuc.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_F10, 0));
        mniKetThuc.setFont(new java.awt.Font("Open Sans", 0, 14)); // NOI18N
        mniKetThuc.setIcon(new javax.swing.ImageIcon(getClass().getResource("/poly/app/view/icon/Stop.png"))); // NOI18N
        mniKetThuc.setText("Kết thúc");
        mniKetThuc.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mniKetThucActionPerformed(evt);
            }
        });
        jMenu2.add(mniKetThuc);

        jMenuBar1.add(jMenu2);

        jMenu3.setText("Quản lý");

        mniQLNguoiHoc.setFont(new java.awt.Font("Open Sans", 0, 14)); // NOI18N
        mniQLNguoiHoc.setIcon(new javax.swing.ImageIcon(getClass().getResource("/poly/app/view/icon/Conference.png"))); // NOI18N
        mniQLNguoiHoc.setText("Người học");
        jMenu3.add(mniQLNguoiHoc);

        mniQLChuyenDe.setFont(new java.awt.Font("Open Sans", 0, 14)); // NOI18N
        mniQLChuyenDe.setIcon(new javax.swing.ImageIcon(getClass().getResource("/poly/app/view/icon/Lists.png"))); // NOI18N
        mniQLChuyenDe.setText("Chuyên đề");
        jMenu3.add(mniQLChuyenDe);

        mniQLKhoaHoc.setFont(new java.awt.Font("Open Sans", 0, 14)); // NOI18N
        mniQLKhoaHoc.setIcon(new javax.swing.ImageIcon(getClass().getResource("/poly/app/view/icon/Certificate.png"))); // NOI18N
        mniQLKhoaHoc.setText("Khoá học");
        jMenu3.add(mniQLKhoaHoc);

        mniQLNhanVien.setFont(new java.awt.Font("Open Sans", 0, 14)); // NOI18N
        mniQLNhanVien.setIcon(new javax.swing.ImageIcon(getClass().getResource("/poly/app/view/icon/User group.png"))); // NOI18N
        mniQLNhanVien.setText("Nhân viên");
        jMenu3.add(mniQLNhanVien);

        jMenuBar1.add(jMenu3);

        mniNguoiHocTungNam.setText("Thống kê");

        mniTKNguoiHoc.setFont(new java.awt.Font("Open Sans", 0, 14)); // NOI18N
        mniTKNguoiHoc.setIcon(new javax.swing.ImageIcon(getClass().getResource("/poly/app/view/icon/Clien list.png"))); // NOI18N
        mniTKNguoiHoc.setText("Người học từng năm");
        mniNguoiHocTungNam.add(mniTKNguoiHoc);
        mniNguoiHocTungNam.add(jSeparator3);

        mniTKBangDiemKhoa.setFont(new java.awt.Font("Open Sans", 0, 14)); // NOI18N
        mniTKBangDiemKhoa.setIcon(new javax.swing.ImageIcon(getClass().getResource("/poly/app/view/icon/Card file.png"))); // NOI18N
        mniTKBangDiemKhoa.setText("Bảng điểm khoá");
        mniNguoiHocTungNam.add(mniTKBangDiemKhoa);

        mniTKDiemTungKhoaHoc.setFont(new java.awt.Font("Open Sans", 0, 14)); // NOI18N
        mniTKDiemTungKhoaHoc.setIcon(new javax.swing.ImageIcon(getClass().getResource("/poly/app/view/icon/Bar chart.png"))); // NOI18N
        mniTKDiemTungKhoaHoc.setText("Điểm từng khoá học");
        mniNguoiHocTungNam.add(mniTKDiemTungKhoaHoc);
        mniNguoiHocTungNam.add(jSeparator4);

        mniTKDoanhThu.setFont(new java.awt.Font("Open Sans", 0, 14)); // NOI18N
        mniTKDoanhThu.setIcon(new javax.swing.ImageIcon(getClass().getResource("/poly/app/view/icon/Dollar.png"))); // NOI18N
        mniTKDoanhThu.setText("Doanh thu từng chuyên đề");
        mniNguoiHocTungNam.add(mniTKDoanhThu);

        jMenuBar1.add(mniNguoiHocTungNam);

        jMenu5.setText("Trợ giúp");

        mniHuongDanSuDung.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_F1, 0));
        mniHuongDanSuDung.setFont(new java.awt.Font("Open Sans", 0, 14)); // NOI18N
        mniHuongDanSuDung.setIcon(new javax.swing.ImageIcon(getClass().getResource("/poly/app/view/icon/Globe.png"))); // NOI18N
        mniHuongDanSuDung.setText("Hướng dẫn sử dụng");
        jMenu5.add(mniHuongDanSuDung);
        jMenu5.add(jSeparator5);

        mniGioiThieuSanPham.setFont(new java.awt.Font("Open Sans", 0, 14)); // NOI18N
        mniGioiThieuSanPham.setIcon(new javax.swing.ImageIcon(getClass().getResource("/poly/app/view/icon/Brick house.png"))); // NOI18N
        mniGioiThieuSanPham.setText("Giới thiệu sản phẩm");
        jMenu5.add(mniGioiThieuSanPham);

        jMenuBar1.add(jMenu5);

        setJMenuBar(jMenuBar1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, 1200, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, 679, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnDangXuatActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDangXuatActionPerformed
        this.setVisible(false);
        ShareHelper.logOut();
        openDangNhapDialog();
        this.setVisible(true);
    }//GEN-LAST:event_btnDangXuatActionPerformed

    private void btnKetThucActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnKetThucActionPerformed
        boolean isConfirm = DialogHelper.confirm(this, "Bạn có muốn đóng chương trình?");
        if (isConfirm) {
            System.exit(0);
        }
    }//GEN-LAST:event_btnKetThucActionPerformed

    private void mniDoiMatKhauActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mniDoiMatKhauActionPerformed
        this.setVisible(false);
        openDoiMatKhauDialog();
        this.setVisible(true);
    }//GEN-LAST:event_mniDoiMatKhauActionPerformed

    private void btnNhanVienActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNhanVienActionPerformed
        nhanVienJFrame.requestFocus();
        nhanVienJFrame.setVisible(true);
    }//GEN-LAST:event_btnNhanVienActionPerformed

    private void formWindowActivated(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowActivated
        nhanVienJFrame.setAlwaysOnTop(true);
        nhanVienJFrame.requestFocus();
    }//GEN-LAST:event_formWindowActivated

    private void formWindowDeactivated(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowDeactivated
        nhanVienJFrame.setAlwaysOnTop(false);
    }//GEN-LAST:event_formWindowDeactivated

    private void btnKhoaHocActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnKhoaHocActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnKhoaHocActionPerformed

    private void btnChuyenDeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnChuyenDeActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnChuyenDeActionPerformed

    private void btnNguoiHocActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNguoiHocActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnNguoiHocActionPerformed

    private void mniDangNhapActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mniDangNhapActionPerformed
        this.btnDangXuatActionPerformed(evt);
    }//GEN-LAST:event_mniDangNhapActionPerformed

    private void mniLDangXuatActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mniLDangXuatActionPerformed
        this.btnDangXuatActionPerformed(evt);
    }//GEN-LAST:event_mniLDangXuatActionPerformed

    private void mniKetThucActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mniKetThucActionPerformed
        this.btnKetThucActionPerformed(evt);
    }//GEN-LAST:event_mniKetThucActionPerformed

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
            java.util.logging.Logger.getLogger(MainJFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(MainJFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(MainJFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(MainJFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new MainJFrame().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnChuyenDe;
    private javax.swing.JButton btnDangXuat;
    private javax.swing.JButton btnHuongDan;
    private javax.swing.JButton btnKetThuc;
    private javax.swing.JButton btnKhoaHoc;
    private javax.swing.JButton btnNguoiHoc;
    private javax.swing.JButton btnNhanVien;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenu jMenu2;
    private javax.swing.JMenu jMenu3;
    private javax.swing.JMenu jMenu5;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPopupMenu.Separator jSeparator1;
    private javax.swing.JSeparator jSeparator10;
    private javax.swing.JSeparator jSeparator11;
    private javax.swing.JToolBar.Separator jSeparator12;
    private javax.swing.JPopupMenu.Separator jSeparator2;
    private javax.swing.JPopupMenu.Separator jSeparator3;
    private javax.swing.JPopupMenu.Separator jSeparator4;
    private javax.swing.JPopupMenu.Separator jSeparator5;
    private javax.swing.JToolBar.Separator jSeparator6;
    private javax.swing.JSeparator jSeparator7;
    private javax.swing.JSeparator jSeparator8;
    private javax.swing.JToolBar.Separator jSeparator9;
    private javax.swing.JToolBar jToolBar1;
    private javax.swing.JLabel lblDongHo;
    private javax.swing.JLabel lblThongTin;
    private javax.swing.JMenuItem mniDangNhap;
    private javax.swing.JMenuItem mniDoiMatKhau;
    private javax.swing.JMenuItem mniGioiThieuSanPham;
    private javax.swing.JMenuItem mniHuongDanSuDung;
    private javax.swing.JMenuItem mniKetThuc;
    private javax.swing.JMenuItem mniLDangXuat;
    private javax.swing.JMenu mniNguoiHocTungNam;
    private javax.swing.JMenuItem mniQLChuyenDe;
    private javax.swing.JMenuItem mniQLKhoaHoc;
    private javax.swing.JMenuItem mniQLNguoiHoc;
    private javax.swing.JMenuItem mniQLNhanVien;
    private javax.swing.JMenuItem mniTKBangDiemKhoa;
    private javax.swing.JMenuItem mniTKDiemTungKhoaHoc;
    private javax.swing.JMenuItem mniTKDoanhThu;
    private javax.swing.JMenuItem mniTKNguoiHoc;
    // End of variables declaration//GEN-END:variables
}
