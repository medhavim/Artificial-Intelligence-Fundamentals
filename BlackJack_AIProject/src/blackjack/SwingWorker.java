package blackjack;

import javax.swing.SwingUtilities;

public abstract class SwingWorker {
	
	private Object value;
	@SuppressWarnings("unused")
	private Thread thread;
	private SwingWorker.ThreadVar threadVar;

	private static class ThreadVar
	{
		private Thread thread;

		ThreadVar(Thread paramThread)
		{
			this.thread = paramThread;
		}

		synchronized Thread get()
		{
			return this.thread;
		}

		synchronized void clear()
		{
			this.thread = null;
		}
	}

	protected synchronized Object getValue()
	{
		return this.value;
	}

	private synchronized void setValue(Object paramObject)
	{
		this.value = paramObject;
	}

	public abstract Object construct();

	public void finished() {}

	public void interrupt()
	{
		Thread localThread = this.threadVar.get();
		if (localThread != null) {
			localThread.interrupt();
		}
		this.threadVar.clear();
	}

	public Object get()
	{
		for (;;)
		{
			Thread localThread = this.threadVar.get();
			if (localThread == null) {
				return getValue();
			}
			try
			{
				localThread.join();
			}
			catch (InterruptedException localInterruptedException)
			{
				Thread.currentThread().interrupt();
			}
		}
		//	    return null;
	}

	public SwingWorker()
	{
		@SuppressWarnings("unused")
		Runnable local1 = new Runnable()
		{
			public void run()
			{
				SwingWorker.this.finished();
			}
		};
		Runnable local2 = new Runnable()
		{
			private Runnable val$doFinished;

			public void run()
			{
				try
				{
					SwingWorker.this.setValue(SwingWorker.this.construct());
				}
				finally
				{
					SwingWorker.this.threadVar.clear();
				}
				SwingUtilities.invokeLater(this.val$doFinished);
			}
		};
		Thread localThread = new Thread(local2);
		this.threadVar = new SwingWorker.ThreadVar(localThread);
	}

	public void start()
	{
		Thread localThread = this.threadVar.get();
		if (localThread != null) {
			localThread.start();
		}
	}
}
