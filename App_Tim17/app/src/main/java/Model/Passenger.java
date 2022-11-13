package Model;

public class Passenger {
    private String name;
    private String lastname;
    private String phone;
    private String email;
    private String address;
    private int avatar;


    public Passenger(String name, String lastname, String phone, String email, String address, int avatar) {
        this.name = name;
        this.lastname = lastname;
        this.phone = phone;
        this.email = email;
        this.address = address;
        this.avatar = avatar;
    }

    public int getAvatar() {
        return avatar;
    }

    public void setAvatar(int avatar) {
        this.avatar = avatar;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

}
