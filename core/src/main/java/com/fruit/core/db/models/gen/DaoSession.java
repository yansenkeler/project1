package com.fruit.core.db.models.gen;

import android.database.sqlite.SQLiteDatabase;

import java.util.Map;

import de.greenrobot.dao.AbstractDao;
import de.greenrobot.dao.AbstractDaoSession;
import de.greenrobot.dao.identityscope.IdentityScopeType;
import de.greenrobot.dao.internal.DaoConfig;

import com.fruit.core.db.models.gen.washcaradmin;
import com.fruit.core.db.models.gen.config;
import com.fruit.core.db.models.gen.accountPassword;
import com.fruit.core.db.models.gen.msg;

import com.fruit.core.db.models.gen.washcaradminDao;
import com.fruit.core.db.models.gen.configDao;
import com.fruit.core.db.models.gen.accountPasswordDao;
import com.fruit.core.db.models.gen.msgDao;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.

/**
 * {@inheritDoc}
 * 
 * @see de.greenrobot.dao.AbstractDaoSession
 */
public class DaoSession extends AbstractDaoSession {

    private final DaoConfig washcaradminDaoConfig;
    private final DaoConfig configDaoConfig;
    private final DaoConfig accountPasswordDaoConfig;
    private final DaoConfig msgDaoConfig;

    private final washcaradminDao washcaradminDao;
    private final configDao configDao;
    private final accountPasswordDao accountPasswordDao;
    private final msgDao msgDao;

    public DaoSession(SQLiteDatabase db, IdentityScopeType type, Map<Class<? extends AbstractDao<?, ?>>, DaoConfig>
            daoConfigMap) {
        super(db);

        washcaradminDaoConfig = daoConfigMap.get(washcaradminDao.class).clone();
        washcaradminDaoConfig.initIdentityScope(type);

        configDaoConfig = daoConfigMap.get(configDao.class).clone();
        configDaoConfig.initIdentityScope(type);

        accountPasswordDaoConfig = daoConfigMap.get(accountPasswordDao.class).clone();
        accountPasswordDaoConfig.initIdentityScope(type);

        msgDaoConfig = daoConfigMap.get(msgDao.class).clone();
        msgDaoConfig.initIdentityScope(type);

        washcaradminDao = new washcaradminDao(washcaradminDaoConfig, this);
        configDao = new configDao(configDaoConfig, this);
        accountPasswordDao = new accountPasswordDao(accountPasswordDaoConfig, this);
        msgDao = new msgDao(msgDaoConfig, this);

        registerDao(washcaradmin.class, washcaradminDao);
        registerDao(config.class, configDao);
        registerDao(accountPassword.class, accountPasswordDao);
        registerDao(msg.class, msgDao);
    }
    
    public void clear() {
        washcaradminDaoConfig.getIdentityScope().clear();
        configDaoConfig.getIdentityScope().clear();
        accountPasswordDaoConfig.getIdentityScope().clear();
        msgDaoConfig.getIdentityScope().clear();
    }

    public washcaradminDao getWashcaradminDao() {
        return washcaradminDao;
    }

    public configDao getConfigDao() {
        return configDao;
    }

    public accountPasswordDao getAccountPasswordDao() {
        return accountPasswordDao;
    }

    public msgDao getMsgDao() {
        return msgDao;
    }

}
