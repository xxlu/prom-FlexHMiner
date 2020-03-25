package org.processmining.aam.exp.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.Executor;

import org.processmining.framework.connections.Connection;
import org.processmining.framework.connections.ConnectionCannotBeObtained;
import org.processmining.framework.connections.ConnectionID;
import org.processmining.framework.connections.ConnectionManager;
import org.processmining.framework.plugin.PluginContext;
import org.processmining.framework.plugin.PluginContextID;
import org.processmining.framework.plugin.PluginDescriptor;
import org.processmining.framework.plugin.PluginExecutionResult;
import org.processmining.framework.plugin.PluginManager;
import org.processmining.framework.plugin.PluginParameterBinding;
import org.processmining.framework.plugin.ProMFuture;
import org.processmining.framework.plugin.Progress;
import org.processmining.framework.plugin.RecursiveCallException;
import org.processmining.framework.plugin.events.ConnectionObjectListener;
import org.processmining.framework.plugin.events.Logger.MessageLevel;
import org.processmining.framework.plugin.events.PluginLifeCycleEventListener.List;
import org.processmining.framework.plugin.events.ProgressEventListener.ListenerList;
import org.processmining.framework.plugin.impl.FieldSetException;
import org.processmining.framework.providedobjects.ProvidedObjectManager;
import org.processmining.framework.util.Pair;

import sun.reflect.generics.reflectiveObjects.NotImplementedException;

public class ExpDummyPluginContext implements PluginContext {

	private final Progress progress = new Progress() {

		public void setMinimum(int value) {
		}

		public void setMaximum(int value) {
		}

		public void setValue(int value) {
		}

		public void setCaption(String message) {
		}

		public String getCaption() {
			throw new NotImplementedException();
		}

		public int getValue() {
			throw new NotImplementedException();
		}

		public void inc() {
			System.out.print(".");
		}

		public void setIndeterminate(boolean makeIndeterminate) {
		}

		public boolean isIndeterminate() {
			throw new NotImplementedException();
		}

		public int getMinimum() {
			throw new NotImplementedException();
		}

		public int getMaximum() {
			throw new NotImplementedException();
		}

		public boolean isCancelled() {
			return false;
		}

		public void cancel() {
		}

	};

	public PluginManager getPluginManager() {
		throw new NotImplementedException();

	}

	public ProvidedObjectManager getProvidedObjectManager() {
		throw new NotImplementedException();

	}

	private ConnectionManager connectionMgr = new ExpDummyConnectionManager();
	public ConnectionManager getConnectionManager() {
		return connectionMgr;
	}
	
	public <T extends Connection> T addConnection(T c) {
		return connectionMgr.addConnection(c);
	}
	
	public static class ExpDummyConnectionManager implements ConnectionManager {
        private final Map<ConnectionID, Connection> connections = new HashMap<ConnectionID, Connection>();

        public ExpDummyConnectionManager() {
        }

        public void setEnabled(boolean isEnabled) {
        }

        public boolean isEnabled() {
            return false;
        }

        public <T extends Connection> T getFirstConnection(Class<T> connectionType, PluginContext context,
                                                           Object... objects) throws ConnectionCannotBeObtained {
            Iterator<Map.Entry<ConnectionID, Connection>> it = connections.entrySet().iterator();
            while (it.hasNext()) {
                Entry<ConnectionID, Connection> entry = it.next();
                Connection c = entry.getValue();
                if (((connectionType == null) || connectionType.isAssignableFrom(c.getClass()))
                        && c.containsObjects(objects)) {
                    return (T) c;
                }
            }
            throw new ConnectionCannotBeObtained("Connections can't be obtained in dummy testing", connectionType,
                    objects);
        }

        public <T extends Connection> Collection<T> getConnections(Class<T> connectionType, PluginContext context,
                                                                   Object... objects) throws ConnectionCannotBeObtained {
            Collection<T> validConnections = new ArrayList<>();
            Iterator<Map.Entry<ConnectionID, Connection>> it = connections.entrySet().iterator();
            while (it.hasNext()) {
                Entry<ConnectionID, Connection> entry = it.next();
                Connection c = entry.getValue();
                if (((connectionType == null) || connectionType.isAssignableFrom(c.getClass()))
                        && c.containsObjects(objects)) {
                    validConnections.add((T) c);
                }
            }
            return validConnections;
        }

        public org.processmining.framework.plugin.events.ConnectionObjectListener.ListenerList getConnectionListeners() {
            org.processmining.framework.plugin.events.ConnectionObjectListener.ListenerList list = new ConnectionObjectListener.ListenerList();
            return list;
        }

        public Collection<ConnectionID> getConnectionIDs() {
            java.util.List<ConnectionID> list = new ArrayList<>();
            return list;
        }

        public Connection getConnection(ConnectionID id) throws ConnectionCannotBeObtained {
            if (connections.containsKey(id)) {
                return connections.get(id);
            }
            throw new ConnectionCannotBeObtained("No connection with id " + id.toString(), null);
        }

        public void clear() {
            this.connections.clear();
        }

        public <T extends Connection> T addConnection(T connection) {
            connections.put(connection.getID(), connection);
            connection.setManager(this);
            return connection;
        }
    }

	public PluginContextID createNewPluginContextID() {
		throw new NotImplementedException();

	}

	public void invokePlugin(PluginDescriptor plugin, int index, Object... objects) {
		throw new NotImplementedException();

	}

	public void invokeBinding(PluginParameterBinding binding, Object... objects) {
		throw new NotImplementedException();

	}

	public Class<? extends PluginContext> getPluginContextType() {
		throw new NotImplementedException();

	}

	public <T, C extends Connection> Collection<T> tryToFindOrConstructAllObjects(Class<T> type,
			Class<C> connectionType, String role, Object... input) throws ConnectionCannotBeObtained {
		throw new NotImplementedException();

	}

	public <T, C extends Connection> T tryToFindOrConstructFirstObject(Class<T> type, Class<C> connectionType,
			String role, Object... input) throws ConnectionCannotBeObtained {
		throw new NotImplementedException();

	}

	public <T, C extends Connection> T tryToFindOrConstructFirstNamedObject(Class<T> type, String name,
			Class<C> connectionType, String role, Object... input) throws ConnectionCannotBeObtained {
		throw new NotImplementedException();

	}

	public PluginContext createChildContext(String label) {
		throw new NotImplementedException();

	}

	public Progress getProgress() {
		return progress;
	}

	public ListenerList getProgressEventListeners() {
		throw new NotImplementedException();

	}

	public List getPluginLifeCycleEventListeners() {
		throw new NotImplementedException();

	}

	public PluginContextID getID() {
		throw new NotImplementedException();

	}

	public String getLabel() {
		throw new NotImplementedException();

	}

	public Pair<PluginDescriptor, Integer> getPluginDescriptor() {
		throw new NotImplementedException();

	}

	public PluginContext getParentContext() {
		throw new NotImplementedException();

	}

	public java.util.List<PluginContext> getChildContexts() {
		throw new NotImplementedException();
	}

	public PluginExecutionResult getResult() {
		throw new NotImplementedException();
	}

	public ProMFuture<?> getFutureResult(int i) {
		throw new NotImplementedException();
	}

	public Executor getExecutor() {
		throw new NotImplementedException();
	}

	public boolean isDistantChildOf(PluginContext context) {
		throw new NotImplementedException();
	}

	public void setFuture(PluginExecutionResult resultToBe) {
		throw new NotImplementedException();

	}

	public void setPluginDescriptor(PluginDescriptor descriptor, int methodIndex) throws FieldSetException,
			RecursiveCallException {
		throw new NotImplementedException();

	}

	public boolean hasPluginDescriptorInPath(PluginDescriptor descriptor, int methodIndex) {
		throw new NotImplementedException();
	}

	public void log(String message, MessageLevel level) {
		System.out.println(message);
	}

	public void log(String message) {
		System.out.println(message);
	}

	public void log(Throwable exception) {
		exception.printStackTrace();
	}

	public org.processmining.framework.plugin.events.Logger.ListenerList getLoggingListeners() {
		throw new NotImplementedException();
	}

	public PluginContext getRootContext() {
		throw new NotImplementedException();
	}

	public boolean deleteChild(PluginContext child) {
		throw new NotImplementedException();
	}



	public void clear() {
		throw new NotImplementedException();

	}

}