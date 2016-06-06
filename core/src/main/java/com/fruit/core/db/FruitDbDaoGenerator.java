/*
 * Copyright (C) 2011 Markus Junginger, greenrobot (http://greenrobot.de)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.fruit.core.db;

import de.greenrobot.daogenerator.DaoGenerator;
import de.greenrobot.daogenerator.Entity;
import de.greenrobot.daogenerator.Schema;

/**
 * Generates entities and DAOs for the example project DaoExample.
 * <p/>
 * Run it as a Java application (not Android).
 *
 * @author Markus
 * 执行此main函数来自动生成数据库操作dao
 */
public class FruitDbDaoGenerator {
    final static String generatePath = "core/src/main/java";
    final static String getGenerateSchemaPath = "com.fruit.core.db.models.gen";

    public static void main(String[] args) throws Exception {
        Schema schema = new Schema(2, getGenerateSchemaPath);

        addUser(schema);
        addConfig(schema);
        addAccountPassword(schema);
        addMsg(schema);

        new DaoGenerator().generateAll(schema, generatePath);
    }

//    private static void addChannelId(Schema schema) {
//        Entity config = schema.addEntity("ChannelId");
//        config.addStringProperty("key");
//        config.addStringProperty("value");
//    }

    //配置表
    private static void addConfig(Schema schema) {
        Entity config = schema.addEntity("config");
        config.addStringProperty("key");
        config.addStringProperty("value");
    }


    //用户表
    private static void addUser(Schema schema) {
        Entity user = schema.addEntity("washcaradmin");
        user.addStringProperty("washCarId");
        user.addStringProperty("name");
        user.addStringProperty("mobile");
        user.addStringProperty("carLoginId");
        user.addStringProperty("password");
    }

    /**
     * 登录时记住密码的账号和密码
     * @param mSchema
     */
    private static void addAccountPassword(Schema mSchema)
    {
        Entity accountPassword = mSchema.addEntity("accountPassword");
        accountPassword.addIdProperty().primaryKey().autoincrement();
        accountPassword.addStringProperty("account");
        accountPassword.addStringProperty("password");
        accountPassword.addBooleanProperty("lastLogin");
    }

    private static void addMsg(Schema schema){
        Entity msg = schema.addEntity("msg");
        msg.addIdProperty().primaryKey().autoincrement();
        msg.addStringProperty("title");
        msg.addStringProperty("description");
        msg.addStringProperty("billno");
        msg.addBooleanProperty("isRead");
    }

}
