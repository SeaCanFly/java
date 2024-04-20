import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardWatchEventKinds;
import java.nio.file.WatchEvent;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;
public class FileWatcher 
{
	public static void main(String[] args) throws Exception 
	{
		// 获取文件系统的 WatchService
		WatchService watcher = FileSystems.getDefault().newWatchService();
		// 注册要监视的目录
		Path dir = Paths.get("D:/Temp");
		dir.register(watcher, StandardWatchEventKinds.ENTRY_CREATE, StandardWatchEventKinds.ENTRY_DELETE,
				StandardWatchEventKinds.ENTRY_MODIFY);
		// 循环监听文件系统变化
		while (true) 
		{
			WatchKey key;
			try 
			{
				// 获取 WatchKey，等待文件系统变化事件
				key = watcher.take();
			} catch (InterruptedException ex) 
			{
				return;
			}
			// 处理文件系统变化事件
			for (WatchEvent<?> event : key.pollEvents()) 
			{
				WatchEvent.Kind<?> kind = event.kind();

				// 获取事件发生的路径
				@SuppressWarnings("unchecked")
				WatchEvent<Path> ev = (WatchEvent<Path>) event;
				Path fileName = ev.context();

				// 输出事件类型和发生的文件路径
				System.out.println(kind.name() + ": " + fileName);
			}
			// 重置 WatchKey，以便继续接收文件系统变化事件
			boolean valid = key.reset();
			if (!valid) 
			{
				// 如果 WatchKey 失效，则退出循环
				break;
			}
		}
	}
}
