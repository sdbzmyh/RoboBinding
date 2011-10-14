package robobinding.value;

import java.beans.PropertyChangeListener;

import robobinding.beans.ExtendedPropertyChangeSupport;


public abstract class AbstractValueModel<T> implements ValueModel<T>
{
	private static final String PROPERTY_VALUE = "value";
	
	private T value;
	private ExtendedPropertyChangeSupport propertyChangeSupport;
	
	public AbstractValueModel(T value, boolean checkIdentity)
	{
		this.value = value;
		
		propertyChangeSupport = new ExtendedPropertyChangeSupport(this, checkIdentity);
	}
	public AbstractValueModel(T value)
	{
		this(value, false);
	}
	@Override
	public T getValue()
	{
		return value;
	}
	@Override
	public void setValue(T newValue)
	{
		T oldValue = getValue();
		if (oldValue == newValue)
			return;
		value = newValue;
		fireValueChange(oldValue, newValue);
	}
	private void fireValueChange(Object oldValue, Object newValue)
	{
		propertyChangeSupport.firePropertyChange(PROPERTY_VALUE, oldValue, newValue);
	}
	
	@Override
	public final void addValueChangeListener(PropertyChangeListener listener)
	{
		propertyChangeSupport.addPropertyChangeListener(PROPERTY_VALUE, listener);
	}

	@Override
	public final void removeValueChangeListener(PropertyChangeListener listener)
	{
		propertyChangeSupport.removePropertyChangeListener(PROPERTY_VALUE, listener);
	}
	
	@Override
	public String toString()
	{
		return getClass().getName() + "[" + paramString() + "]";
	}

	protected String paramString()
	{
		return "value=" + valueString();
	}
	
	protected String valueString()
	{
		try
		{
			Object value = getValue();
			return value == null ? "null" : value.toString();
		} catch (Exception e)
		{
			return "Can't read";
		}
	}
}
