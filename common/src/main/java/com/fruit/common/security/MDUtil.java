package com.fruit.common.security;



import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MDUtil
{

	public static void main(String[] args)
	{
		System.out.println(authPassword("11111"));
		
	}
	
    /**
     * 加密算码
     * @param password
     * @return
     * @throws java.security.NoSuchAlgorithmException
     */
    public static String authPassword(String password)
    {
        try
        {
            MessageDigest md = MessageDigest.getInstance("SHA1");
            md.update(password.getBytes());
            return bytes2Hex(md.digest()).toUpperCase();
        }
        catch (NoSuchAlgorithmException e)
        {
            return "";
        }
    }

    public static String bytes2Hex(byte[] bts)
    {
        String des = "";
        String tmp = null;
        for (int i = 0; i < bts.length; i++)
        {
            tmp = (Integer.toHexString(bts[i] & 0xFF));
            if (tmp.length() == 1)
            {
                des += "0";
            }
            des += tmp;
        }
        return des;
    }
    
    public static String jm2Md(String password){
    	return authPassword(authPassword(authPassword(password.toLowerCase())));
    }

}
