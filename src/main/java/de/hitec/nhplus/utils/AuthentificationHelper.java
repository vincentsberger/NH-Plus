package de.hitec.nhplus.utils;


import java.util.List;

import de.hitec.nhplus.datastorage.CaregiverDao;
import de.hitec.nhplus.datastorage.DaoFactory;
import de.hitec.nhplus.model.Caregiver;

public class AuthentificationHelper {
    private String username;
    private String password;
    private List<Caregiver> caregivers;

    public AuthentificationHelper() {
        this.username = "admin";
        this.password = "admin123";
    }

    public boolean isValidLogin(String username, String password) {
        /** check fot user from caregiver table from DB */
        this.fetchCaregiverUserCredentialsFromDB();
        for (Caregiver pfleger : this.caregivers) {
            if(username.equals(pfleger.getUsername())) {
                if (password.equals(pfleger.getPassword())) {
                    return true;
                }
            }
        }
        /** check for local admin */
        if (username.equals(this.username) && password.equals(this.password)) {
            return true;
        }
        return false;
    }

    public void fetchCaregiverUserCredentialsFromDB() {
        CaregiverDao cDao = DaoFactory.getDaoFactory().createCaregiverDao();
        try {
            this.caregivers = cDao.readAll();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
