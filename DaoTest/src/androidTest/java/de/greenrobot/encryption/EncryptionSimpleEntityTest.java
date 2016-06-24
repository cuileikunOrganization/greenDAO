package de.greenrobot.encryption;

import net.sqlcipher.database.SQLiteConstraintException;

import java.util.List;

import de.greenrobot.dao.database.Database;
import de.greenrobot.daotest.SimpleEntity;
import de.greenrobot.daotest.SimpleEntityDao.Properties;
import de.greenrobot.daotest.entity.SimpleEntityTest;

public class EncryptionSimpleEntityTest extends SimpleEntityTest {
    @Override
    protected Database createDatabase() {
        return EncryptedDbUtils.createDatabase(getContext(), null, "password");
    }

    @Override
    public void testInsertTwice() {
        try {
            super.testInsertTwice();
            fail("Expected SQLCipher exception");
        } catch (SQLiteConstraintException ex) {
            // OK, expected
        }
    }

    public void testEncryptedDbUsed() {
        EncryptedDbUtils.assertEncryptedDbUsed(db);
    }

    public void testOrderAscString() {
        // SQLCipher 3.5.0 does not understand "COLLATE LOCALIZED ASC" and crashed here initially
        List<SimpleEntity> result = dao.queryBuilder().orderAsc(Properties.SimpleString).list();
        assertEquals(0, result.size());
    }
}
