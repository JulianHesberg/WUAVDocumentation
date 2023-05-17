package dal;

import be.Project;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ProjectDAO {

    DataBaseConnection dbc = DataBaseConnection.getInstance();

    public List<Project> getAllProjects() throws SQLException {
        List<Project> allProjects = new ArrayList<>();
        String sql = "SELECT * FROM Project";
        try (Connection connection = dbc.getConnection()) {
            PreparedStatement ps = connection.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                int id = rs.getInt("project_id");
                String name = rs.getString("project_name");
                String dateLastVisited = rs.getString("date_last_visited");
                String customerName = rs.getString("customer_name");
                String companyAddress = rs.getString("company_address");
                int zipCode = rs.getInt("zip_code");
                int type = rs.getInt("company_type");
                Project project = new Project(id, name, dateLastVisited, customerName, companyAddress, zipCode, type);
                allProjects.add(project);
            }
        }
        return allProjects;
    }
    public List<Project> getUserProjects(int userId) throws SQLException {
        List<Project> userProjects = new ArrayList<>();
        String sql = "SELECT Project.project_id, project_name, Project.date_last_visited, customer_name, \n" +
                "company_address, company_type, [User].user_id FROM Project\n" +
                "INNER JOIN UserProject\n" +
                "ON Project.project_id = UserProject.project_id\n" +
                "RIGHT JOIN [User]\n" +
                "ON UserProject.user_id = [User].[user_id]\n" +
                "WHERE [User].[user_id] = " + userId + ";";
        try (Connection connection = dbc.getConnection()) {
            PreparedStatement ps = connection.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                int id = rs.getInt("project_id");
                String name = rs.getString("project_name");
                String dateLastVisited = rs.getString("date_last_visited");
                String customerName = rs.getString("customer_name");
                String companyAddress = rs.getString("company_address");
                int zipCode = rs.getInt("zip_code");
                int type = rs.getInt("company_type");
                Project project = new Project(id, name, dateLastVisited, customerName, companyAddress,zipCode, type);
                userProjects.add(project);
            }
        }
        return userProjects;
    }

    public void createProject(Project project) throws SQLException{
        String name = project.getName();
        String dateLastVisited = project.getDateLastVisited();
        String customerName = project.getCustomerName();
        String companyAddress = project.getCompanyAddress();
        int companyType = project.getCompanyType();
        int zipCode = project.getZipCode();

        String sql = "INSERT INTO Project (project_name, date_last_visited, customer_name, company_address, company_type, zip_code)" +
                " VALUES (?,?,?,?,?,?)";

        try (Connection con = dbc.getConnection();) {
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, name);
            ps.setString(2, dateLastVisited);
            ps.setString(3, customerName);
            ps.setString(4, companyAddress);
            ps.setInt(5, companyType);
            ps.setInt(6, zipCode);
            ps.execute();
        }
    }

    public boolean deleteProject(Project project) throws SQLException{
        try (Connection con = dbc.getConnection()) {
            int id = project.getId();
            String sql = "DELETE FROM Project WHERE (project_id=?)";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, id);
            int result = ps.executeUpdate();
            if (result > 0)
                return true;
        }
        return false;
    }

    public void deleteFromProjectFromUserProject(Project project) throws SQLException{
        int id = project.getId();
        String sql = "DELETE FROM UserProject WHERE (project_id=?)";
        try (Connection con = dbc.getConnection();) {
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, id);
            ps.execute();
        }
    }

    public void deleteMultipleProjectsFromUserProject(List<Integer> ids) throws SQLException {
        String sql = "DELETE FROM UserProject WHERE project_id = ?";
        try (Connection con = dbc.getConnection();) {
            PreparedStatement statement = con.prepareStatement(sql);
            List<Integer> idsToDelete = ids;
            for (int project_id : idsToDelete) {
                statement.setInt(1, project_id);
                statement.addBatch();
            }
            statement.executeBatch();
        }
    }
    public void deleteMultipleProjects(List<Integer> ids) throws SQLException {
        String sql = "DELETE FROM Project WHERE project_id = ?";
        try (Connection con = dbc.getConnection();) {
            PreparedStatement statement = con.prepareStatement(sql);
            List<Integer> idsToDelete = ids;
            for (int project_id : idsToDelete) {
                statement.setInt(1, project_id);
                statement.addBatch();
            }
            int[] rowsAffected = statement.executeBatch();
            System.out.println(rowsAffected.length + " rows deleted.");
        }
    }

    public void updateProject(Project project) throws SQLException{
        int id = project.getId();
        String name = project.getName();
        String dateLastVisited = project.getDateLastVisited();
        String customerName = project.getCustomerName();
        String companyAddress = project.getCompanyAddress();
        int companyType = project.getCompanyType();

        String sql = "Update Project SET project_name = ?, date_last_visited = ?, customer_name = ?,\n" +
                "company_address = ?, company_type = ? WHERE project_id = ?";

        try (Connection con = dbc.getConnection();) {
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, name);
            ps.setString(2, dateLastVisited);
            ps.setString(3, customerName);
            ps.setString(4, companyAddress);
            ps.setInt(5, companyType);
            ps.setInt(6, id);
            ps.execute();
        }
    }
}
