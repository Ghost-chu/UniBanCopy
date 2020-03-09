package com.mcsunnyside.unibancopy.module;

import litebans.api.Database;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class LiteBansModule implements BanManager {
    @Override
    public Set<UUID> fetchBanList() {
        if (Database.get() == null) return new HashSet<>();

        String query = "SELECT * FROM {bans}";
        Set<UUID> bannedUUID = new HashSet<>();
        try (PreparedStatement st = Database.get().prepareStatement(query)) {
            try (ResultSet result = st.executeQuery()) {
                while (result.next()) {
                    String uuidStr = result.getString("uuid");

                    try {
                        UUID uuid = UUID.fromString(uuidStr);
                        // check if removed or expired
                        // Fix: Logical error
                        if (!Database.get().isPlayerBanned(uuid, null)) continue;
                        bannedUUID.add(uuid);
                    } catch (IllegalArgumentException ignore) {
                    }
                    //System.out.println(uuidStr);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return bannedUUID;
    }
}
