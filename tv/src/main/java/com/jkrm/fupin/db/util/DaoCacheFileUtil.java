package com.jkrm.fupin.db.util;


import com.jkrm.fupin.base.MyApp;
import com.jkrm.fupin.bean.CacheFileBean;
import com.jkrm.fupin.db.CacheFileBeanDao;
import com.jkrm.fupin.db.DaoManager;
import com.jkrm.fupin.util.MyLog;
import com.jkrm.fupin.util.MyUtil;

import org.greenrobot.greendao.query.QueryBuilder;

import java.util.List;

/**
 * Created by hzw on 2017/11/16.
 */

public class DaoCacheFileUtil {

    private static final String TAG = DaoCacheFileUtil.class.getSimpleName();
    private DaoManager mDaoManager;
    private static DaoCacheFileUtil instance;

    public static DaoCacheFileUtil getInstance() {
        if(instance == null)
            instance = new DaoCacheFileUtil();
        return instance;
    }

    private DaoCacheFileUtil() {
        mDaoManager = DaoManager.getInstance();
        mDaoManager.init(MyApp.getInstance());
    }


    /**
     * 完成 Bean记录的插入，如果表未创建，先创建 Bean表
     * @param bean
     * @return
     */
    public boolean insertBean(CacheFileBean bean){
        boolean flag = false;
        try {
            flag = mDaoManager.getDaoSession().getCacheFileBeanDao().insert(bean) == -1 ? false : true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        MyLog.i(TAG, "insert Bean :" + flag + "-->" + bean.toString());
        return flag;
    }

    /**
     * 插入多条数据，在子线程操作
     * @param beanList
     * @return
     */
    public boolean insertBeanList(final List<CacheFileBean> beanList) {
        boolean flag = false;
        try {
            mDaoManager.getDaoSession().runInTx(new Runnable() {
                @Override
                public void run() {
                    for (CacheFileBean bean : beanList) {
                        mDaoManager.getDaoSession().insertOrReplace(bean);
                    }
                }
            });
            flag = true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return flag;
    }

    /**
     * 修改一条数据
     * @param bean
     * @return
     */
    public boolean updateBean(CacheFileBean bean){
        boolean flag = false;
        try {
            mDaoManager.getDaoSession().update(bean);
            flag = true;
        }catch (Exception e){
            e.printStackTrace();
        }
        return flag;
    }

    /**
     * 修改一条数据
     * @param url
     * @return
     */
    public boolean updateBean(String url){
        boolean flag = false;
        try {
            CacheFileBean bean = queryBeanByQueryBuilderUrl(url);
            mDaoManager.getDaoSession().update(bean);
            flag = true;
        }catch (Exception e){
            e.printStackTrace();
        }
        return flag;
    }

    /**
     * 删除单条记录
     * @param bean
     * @return
     */
    public boolean deleteBean(CacheFileBean bean){
        boolean flag = false;
        try {
            //按照id删除
            mDaoManager.getDaoSession().delete(bean);
            flag = true;
        }catch (Exception e){
            e.printStackTrace();
        }
        return flag;
    }

    /**
     * 删除单条记录
     * @param url
     * @return
     */
    public boolean deleteBeanByUrl(String url){
        boolean flag = false;
        try {
            //按照id删除
            CacheFileBean bean = queryBeanByQueryBuilderUrl(url);
            mDaoManager.getDaoSession().delete(bean);
            flag = true;
        }catch (Exception e){
            e.printStackTrace();
        }
        return flag;
    }

    /**
     * 删除所有记录
     * @return
     */
    public boolean deleteAllBean(){
        boolean flag = false;
        try {
            //按照id删除
            mDaoManager.getDaoSession().deleteAll(CacheFileBean.class);
            flag = true;
        }catch (Exception e){
            e.printStackTrace();
        }
        return flag;
    }

    /**
     * 查询所有记录
     * @return
     */
    public List<CacheFileBean> queryAllBean(){
        return mDaoManager.getDaoSession().loadAll(CacheFileBean.class);
    }

    /**
     * 根据主键id查询记录
     * @param key
     * @return
     */
    public CacheFileBean queryBeanById(long key){
        return mDaoManager.getDaoSession().load(CacheFileBean.class, key);
    }

    /**
     * 根据主键id查询记录
     * @param url
     * @return
     */
    public CacheFileBean queryBeanByUrl(String url){
        return mDaoManager.getDaoSession().load(CacheFileBean.class, url);
    }

    /**
     * 使用native sql进行查询操作
     */

    /**
     * * String sql = "where _id > ?";
     String[] condition = new String[]{"3"};
     List<CacheFileBean> list = mMeiziDaoUtils.queryBeanByNativeSql(sql, condition);
     for (CacheFileBean bean : list) {
     Log.i(TAG, bean.toString());
     }
     */
    public List<CacheFileBean> queryBeanByNativeSql(String sql, String[] conditions){
        return mDaoManager.getDaoSession().queryRaw(CacheFileBean.class, sql, conditions);
    }

    /**
     * 使用queryBuilder进行查询
     */
    public CacheFileBean queryBeanByQueryBuilderUrl(String url){
        QueryBuilder<CacheFileBean> queryBuilder = mDaoManager.getDaoSession().queryBuilder(CacheFileBean.class);
        List<CacheFileBean> list = queryBuilder.where(CacheFileBeanDao.Properties.Url.eq(url)).list();
        if(MyUtil.isEmptyList(list))
            return null;
        else
            return list.get(0);
    }

    /**
     * 使用queryBuilder进行查询
     */
    public List<CacheFileBean> queryListByQueryBuilderUrl(String url){
        QueryBuilder<CacheFileBean> queryBuilder = mDaoManager.getDaoSession().queryBuilder(CacheFileBean.class);
        return queryBuilder.where(CacheFileBeanDao.Properties.Url.eq(url)).list();
    }

    /**
     * 使用queryBuilder进行查询
     */
    public List<CacheFileBean> queryBeanByQueryBuilderOfName(String params){
        QueryBuilder<CacheFileBean> queryBuilder = mDaoManager.getDaoSession().queryBuilder(CacheFileBean.class);
        return queryBuilder.where(CacheFileBeanDao.Properties.FileName.eq(params)).list();
    }
}
