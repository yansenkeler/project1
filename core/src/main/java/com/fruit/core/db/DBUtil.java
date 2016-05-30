package com.fruit.core.db;


import com.fruit.core.db.models.gen.DaoSession;
import com.fruit.core.db.models.gen.accountPassword;
import com.fruit.core.db.models.gen.accountPasswordDao;
import com.fruit.core.db.models.gen.config;
import com.fruit.core.db.models.gen.configDao;
import com.fruit.core.db.models.gen.configDao.Properties;
import com.fruit.core.db.models.gen.msg;
import com.fruit.core.db.models.gen.msgDao;

import java.util.ArrayList;
import java.util.List;

import de.greenrobot.dao.query.QueryBuilder;

/**
 * Created by liangchen on 15/3/14.
 */
public class DBUtil {

    // 设置配置信息
    public static void setConfigValue(String key, String value) {
        DaoSession daoSession = DatabaseManager.getInstance().getDaoSession("config");
        config c = new config();
        c.setKey(key);
        c.setValue(value);
        configDao configDao = daoSession.getConfigDao();
        configDao.queryBuilder().where(Properties.Key.eq(key)).buildDelete().executeDeleteWithoutDetachingEntities();//删除操作
        GreenDaoUtils.insert(daoSession.getConfigDao(), c);//插入操作
        List<config> configs = configDao.queryBuilder().where(Properties.Key.eq(key)).list();
        DatabaseManager.getInstance().closeDatabase();
    }

    // 获取配置信息
    public static String getConfigValue(String key) {
        DaoSession daoSession = DatabaseManager.getInstance().getDaoSession("config");
        configDao configDao = daoSession.getConfigDao();
        List<config> configs =  configDao.queryBuilder().where(Properties.Key.eq(key)).list();
        String value = "";
        if (configs!=null&&configs.size()>0){
            value = configs.get(0).getValue();
        }
        DatabaseManager.getInstance().closeDatabase();
        return value;
    }

    /**
     * 根据账号查询获得密码，没有就返回NULL
     * @param account
     * @return
     */
    public static String getPassword(String account)
    {
        DaoSession mDaoSession = DatabaseManager.getInstance().getDaoSession("accountPassword");
        accountPasswordDao mDao = mDaoSession.getAccountPasswordDao();
        List<accountPassword> mList = mDao.loadAll();
        for (int i=0; i<mList.size(); i++)
        {
            accountPassword mAccountPassword = mList.get(i);
            String mAccount = mAccountPassword.getAccount();
            if (account!=null && mAccount!=null && account.equals(mAccount))
            {
                return mAccountPassword.getPassword();
            }
        }
        DatabaseManager.getInstance().closeDatabase();
        return null;
    }

    /**
     * 插入新的账号密码
     * @param account
     * @param password
     * @param lastLogin
     */
    public static void insertAccountPassword(String account, String password, boolean lastLogin)
    {
        DaoSession mDaoSession = DatabaseManager.getInstance().getDaoSession("accountPassword");
        accountPassword mAccountPassword = new accountPassword();
        mAccountPassword.setAccount(account);
        mAccountPassword.setPassword(password);
        mAccountPassword.setLastLogin(lastLogin);

        accountPasswordDao mDao = mDaoSession.getAccountPasswordDao();
        List<accountPassword> mList = mDao.queryBuilder().where(accountPasswordDao.Properties.Account.eq(account)).list();
        mDao.deleteInTx(mList);
        List<accountPassword> mList1 = mDao.queryBuilder().where(accountPasswordDao.Properties.LastLogin.eq(true)).list();
        for (int i=0; i<mList1.size(); i++)
        {
            mList1.get(i).setLastLogin(false);
        }
        mDao.updateInTx(mList1);
        mDao.insert(mAccountPassword);
        DatabaseManager.getInstance().closeDatabase();
    }

    /**
     *
     * @return
     */
    public static accountPassword getLastLoginAccount()
    {
        DaoSession mDaoSession = DatabaseManager.getInstance().getDaoSession("accountPassword");
        accountPasswordDao mDao = mDaoSession.getAccountPasswordDao();
        QueryBuilder<accountPassword> mList = mDao.queryBuilder().where(accountPasswordDao.Properties.LastLogin.eq(true));
        if (mList.list().size()==1){
            return mList.list().get(0);
        }
        return null;
    }

    public static ArrayList<msg> getMsgs(){
        DaoSession daoSession = DatabaseManager.getInstance().getDaoSession("msg");
        msgDao messageDao = daoSession.getMsgDao();
        ArrayList<msg> shs = (ArrayList<msg>) messageDao.loadAll();
        DatabaseManager.getInstance().closeDatabase();
        return shs;
    }

    public static void setIsRead(String billno, boolean isRead){
        DaoSession mSession = DatabaseManager.getInstance().getDaoSession("msg");
        msgDao mMessageDao = mSession.getMsgDao();
        List<msg> mMessages = mMessageDao.loadAll();
        for (int i=0; i<mMessages.size(); i++){
            msg msg = mMessages.get(i);
            if (msg.getBillno().equals(billno)){
                mMessages.get(i).setIsRead(isRead);
                break;
            }
        }
        mMessageDao.updateInTx(mMessages);
        DatabaseManager.getInstance().closeDatabase();
    }

    public static void setMsg(String title, String description, String billno){
        DaoSession daoSession = DatabaseManager.getInstance().getDaoSession("msg");
        msg data = new msg();
        data.setTitle(title);
        data.setDescription(description);
        data.setBillno(billno);
        data.setIsRead(false);
        msgDao messageDao = daoSession.getMsgDao();
        messageDao.queryBuilder().where(msgDao.Properties.Title.eq(title),
                msgDao.Properties.Description.eq(description),
                msgDao.Properties.Billno.eq(billno),
                msgDao.Properties.IsRead.eq(false))
                .buildDelete().executeDeleteWithoutDetachingEntities();
        GreenDaoUtils.insert(messageDao, data);
        DatabaseManager.getInstance().closeDatabase();
    }
}
