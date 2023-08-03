package awtandswing.bookrecordmanagent;
import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import java.util.ArrayList;
import java.util.Objects;

public class BookFrame {
    Connection con;
    PreparedStatement ps;
    JFrame frame = new JFrame("Book Record Management");
    JPanel insert, view;
    JTabbedPane tabbedPane = new JTabbedPane();
    JLabel id, tit, pri, an, pn;
    JTextField t1, t2, t3, t4, t5;
    JButton save, delete, update;
    JTable table;
    JScrollPane scrollPane;
    DefaultTableModel dtm;
    String[] column = {"BookId", "Title", "Price", "Author", "Publisher"};

    public BookFrame() {

        getConnectionFromMySQL();
        initComponents();

    }

    void initComponents() {


        id = new JLabel("BookId");
        tit = new JLabel("Title");
        pri = new JLabel("Price");
        an = new JLabel("Author");
        pn = new JLabel("Publisher");

        t1 = new JTextField();
        t2 = new JTextField();
        t3 = new JTextField();
        t4 = new JTextField();
        t5 = new JTextField();

        save = new JButton("Save");

        id.setBounds(100, 100, 100, 20);
        tit.setBounds(100, 150, 100, 20);
        pri.setBounds(100, 200, 100, 20);
        an.setBounds(100, 250, 100, 20);
        pn.setBounds(100, 300, 100, 20);

        t1.setBounds(250, 100, 200, 20);
        t2.setBounds(250, 150, 200, 20);
        t3.setBounds(250, 200, 200, 20);
        t4.setBounds(250, 250, 200, 20);
        t5.setBounds(250, 300, 200, 20);

        save.setBounds(100, 350, 100, 30);

        insert = new JPanel();
        insert.setLayout(null);

        insert.add(id);
        insert.add(tit);
        insert.add(pri);
        insert.add(an);
        insert.add(pn);
        insert.add(t1);
        insert.add(t2);
        insert.add(t3);
        insert.add(t4);
        insert.add(t5);
        insert.add(save);

        ArrayList<Book> b = fetchBookRecord();
        setDataOnTable(b);

        update = new JButton("Update");
        update.addActionListener(new UpdateBookRecord());

        delete = new JButton("Delete");
        delete.addActionListener(new DeleteBookRecords());

        scrollPane = new JScrollPane(table);

        view = new JPanel();

        view.add(update);
        view.add(delete);
        view.add(scrollPane);

        tabbedPane.add(insert);
        tabbedPane.add(view);

        tabbedPane.addChangeListener(new TabbedChangeHandler());

        frame.add(tabbedPane);

        frame.setVisible(true);
        frame.setSize(550, 500);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        save.addActionListener(new Save());

    }

    void setDataOnTable(ArrayList<Book> bl) {
        int blSize = bl.size();

        Object[][] obj = new Object[blSize][5];

        for (int i = 0; i < blSize; i++) {
            obj[i][0] = bl.get(i).getBookId();
            obj[i][1] = bl.get(i).getTitle();
            obj[i][2] = bl.get(i).getPrice();
            obj[i][3] = bl.get(i).getAuthor();
            obj[i][4] = bl.get(i).getPublisher();
        }

        table = new JTable();
        dtm = new DefaultTableModel();
        dtm.setColumnCount(5);
        dtm.setRowCount(blSize);
        dtm.setColumnIdentifiers(column);

        for (int i = 0; i < blSize; i++) {
            dtm.setValueAt(obj[i][0], i, 0);
            dtm.setValueAt(obj[i][1], i, 1);
            dtm.setValueAt(obj[i][2], i, 2);
            dtm.setValueAt(obj[i][3], i, 3);
            dtm.setValueAt(obj[i][4], i, 4);
        }

        table.setModel(dtm);

    }

    ArrayList<Book> fetchBookRecord() {
        ArrayList<Book> bl = new ArrayList<>();
        String q = "SELECT * FROM book";

        try {

            ps = con.prepareStatement(q);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Book b = new Book();
                b.setBookId(rs.getInt(1));
                b.setTitle(rs.getString(2));
                b.setPrice(rs.getDouble(3));
                b.setAuthor(rs.getString(4));
                b.setPublisher(rs.getString(5));
                bl.add(b);
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            return bl;
        }
    }

    class Save implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            Book b1 = readFromData();
            String q = "INSERT INTO Book values (?,?,?,?,?)";

            try {

                ps = con.prepareStatement(q);
                ps.setInt(1, b1.getBookId());
                ps.setString(2, b1.getTitle());
                ps.setDouble(3, b1.getPrice());
                ps.setString(4, b1.getAuthor());
                ps.setString(5, b1.getPublisher());

                ps.execute();

                t1.setText("");
                t2.setText("");
                t3.setText("");
                t4.setText("");
                t5.setText("");

            } catch (Exception ex) {
                ex.printStackTrace();
            }

        }

        Book readFromData() {
            Book b1 = new Book();
            b1.setBookId(Integer.parseInt(t1.getText()));
            b1.setTitle(t2.getText());
            b1.setPrice(Double.parseDouble(t3.getText()));
            b1.setAuthor(t4.getText());
            b1.setPublisher(t5.getText());
            return b1;
        }
    }

    void getConnectionFromMySQL() {

        String url = "jdbc:mysql://localhost:3306/db1";
        String id = "root";
        String pass = "admin@123";
        try {

            con = DriverManager.getConnection(url, id, pass);
            System.out.println("Connection Established");

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    class TabbedChangeHandler implements ChangeListener {

        @Override
        public void stateChanged(ChangeEvent e) {

            int index = tabbedPane.getSelectedIndex();

            if (index == 1) {
                ArrayList<Book> bl = fetchBookRecord();
                updateTable(bl);
            }

        }
    }

    private void updateTable(ArrayList<Book> bl) {

        int blSize = bl.size();

        Object[][] obj = new Object[blSize][5];

        for (int i = 0; i < blSize; i++) {
            obj[i][0] = bl.get(i).getBookId();
            obj[i][1] = bl.get(i).getTitle();
            obj[i][2] = bl.get(i).getPrice();
            obj[i][3] = bl.get(i).getAuthor();
            obj[i][4] = bl.get(i).getPublisher();
        }

        dtm.setRowCount(blSize);

        for (int i = 0; i < blSize; i++) {
            dtm.setValueAt(obj[i][0], i, 0);
            dtm.setValueAt(obj[i][1], i, 1);
            dtm.setValueAt(obj[i][2], i, 2);
            dtm.setValueAt(obj[i][3], i, 3);
            dtm.setValueAt(obj[i][4], i, 4);
        }

        table.setModel(dtm);

    }

    class UpdateBookRecord implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {

            ArrayList<Book> ubl = readTableData();

            String q = "UPDATE BOOK SET title = ?, price = ?, author = ?, publisher = ? WHERE bookid = ?";

            try {
                for (int i = 0; i < ubl.size(); i++){

                }
                ps = con.prepareStatement(q);

                for (int i = 0; i < ubl.size(); i++) {
                    ps.setString(1, ubl.get(i).getTitle());
                    ps.setDouble(2, ubl.get(i).getPrice());
                    ps.setString(3, ubl.get(i).getAuthor());
                    ps.setString(4, ubl.get(i).getPublisher());
                    ps.setInt(5, ubl.get(i).getBookId());
                    ps.execute();
                }

            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
        }
    }

    class DeleteBookRecords implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {

            int rowNo = table.getSelectedRow();
            if (rowNo != -1) {

                int id = (int) table.getValueAt(rowNo, 0);

                String s = "DELETE FROM book WHERE bookid = ?";

                try {

                    ps = con.prepareStatement(s);
                    ps.setInt(1, id);
                    ps.execute();

                } catch (SQLException ex) {
                    throw new RuntimeException(ex);
                } finally {
                    ArrayList<Book> bl = fetchBookRecord();
                    updateTable(bl);
                }

            }
        }
    }

    private ArrayList<Book> readTableData() {
        ArrayList<Book> bl = new ArrayList<>();

        for (int i = 0; i < table.getRowCount(); i++) {
            Book b1 = new Book();
            b1.setBookId(Integer.parseInt(table.getValueAt(i, 0).toString()));
            b1.setTitle(table.getValueAt(i, 1).toString());
            b1.setPrice(Double.parseDouble(table.getValueAt(i, 2).toString()));
            b1.setAuthor(table.getValueAt(i, 3).toString());
            b1.setPublisher(table.getValueAt(i, 4).toString());
            bl.add(b1);
        }

        return bl;

    }
}