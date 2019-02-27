package com.kasite.server.verification.module.zk;
import java.util.List;
import org.I0Itec.zkclient.ZkClient;
import org.I0Itec.zkclient.exception.ZkTimeoutException;
import org.apache.zookeeper.Watcher.Event.KeeperState;

/**
 * @author chenw
 * @company yihu.com
 * 2012-10-30上午10:54:57
 */
public class ZkClientWrapper {

	private ZkClient zkClient;
	public ZkClient getZkClient() {
		return zkClient;
	}
	public String getZkUrl() {
		return zkUrl;
	}
	private String zkUrl;
	public ZkClientWrapper(String zkUrl,int connectionTimeout) throws Exception
	{
		
		this.zkUrl=zkUrl;
		try
		{
			zkClient= new ZkClient(zkUrl,connectionTimeout);
			//zkClient.
		}
		catch(ZkTimeoutException  e)
		{
			throw new Exception(zkUrl+"连接超时,"+e.getMessage());
			//zkClient.retryUntilConnected(callable)
		}
	}
	//读数据
	public Object readData(String path)
	{
		return zkClient.readData(path);
	}
	public List<String> getChildren(String path)
	{
		return zkClient.getChildren(path);
	}
	//写数据
	public void writeData(String path, Object object)
	{
		
		zkClient.writeData(path, object);
	}
	public void createPersistent(String path)
	{
		
			zkClient.createPersistent(path);
		
	}
	//写数据
	public void createPersistent(String path, Object object)
	{
		zkClient.createPersistent(path, object);
	}
	public void createEphemeral(String path, Object data)
	{
		zkClient.createEphemeral(path, data);
	}
	public void createEphemeral(String path)
	{
		zkClient.createEphemeral(path);
	}
	public void createEphemeralSequential(String path, Object data)
	{
		zkClient.createEphemeralSequential(path, data);
	}
	public void createPersistentSequential(String path, Object data)
	{
		zkClient.createPersistentSequential(path, data);
	}
	public boolean exists(String path)
	{
		return zkClient.exists(path);
	}
	public boolean deleteRecursive(String path)
	{
		return zkClient.deleteRecursive(path);
	}
	public boolean delete(String path)
	{
		return zkClient.delete(path);
	}
	public void subscribePath(String path,AbstractZKListener listener)
	{
		this.subscribePath(path, listener, 3);
		
	}
	public void subscribeStateChanges(AbstractZkStateListener listener)
	{
		zkClient.subscribeStateChanges(listener);
	}
	public void subscribeStateChanges()
	{
		zkClient.subscribeStateChanges(new AbstractZkStateListener(){

			@Override
			public void handleNewSession() throws Exception {
				// TODO Auto-generated method stub
				System.out.println("handleNewSession");
			}

			@Override
			public void handleStateChanged(KeeperState state) throws Exception {
				// TODO Auto-generated method stub
				System.out.println("handleStateChanged:"+state);
			}

		});
	}
	public int numberOfListeners()
	{
		return zkClient.numberOfListeners();
	}
	public void subscribePath(String path,AbstractZKListener listener,int level)//level=1监听一级节点，2同时监听二级节点，3同时监听三级节点
	{
		zkClient.subscribeChildChanges(path,listener);//监听一级节点下的子节点变化
		zkClient.subscribeDataChanges(path, listener);//监听一级节点的数据变化
		if(level>=2)
		{
			List<String> list2=zkClient.getChildren(path);
			if(list2!=null)
			{
				for (String path2 : list2) 
				{
					zkClient.subscribeChildChanges(path+"/"+path2, listener);//监听二级节点下的子节点变化
					zkClient.subscribeDataChanges(path+"/"+path2, listener);//监听二级节点的数据变化
					if(level>=3)
					{
						List<String> list3=zkClient.getChildren(path+"/"+path2);
						if(list3!=null)
						{
							for (String path3 : list3) 
							{
								zkClient.subscribeChildChanges(path+"/"+path2+"/"+path3, listener);//监听三级节点下的子节点变化
								zkClient.subscribeDataChanges(path+"/"+path2+"/"+path3, listener);//监听三级节点的数据变化
							}
						}
					}
				}
			}
		}
	}
}
