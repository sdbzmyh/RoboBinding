/**
 * Copyright 2011 Cheng Wei, Robert Taylor
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions
 * and limitations under the License.
 */
package org.robobinding.binder;

import java.util.Collection;
import java.util.List;

import org.robobinding.binder.BindingViewInflater.InflatedView;
import org.robobinding.binders.BinderImplementor;
import org.robobinding.binders.BindingContext;
import org.robobinding.binders.PredefinedViewPendingAttributes;

import com.google.common.collect.Lists;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * 
 * @since 1.0
 * @version $Revision: 1.0 $
 * @author Robert Taylor
 */
class BinderImplementorImpl implements BinderImplementor
{
	private final Context context;
	private final BindingContextCreator bindingContextCreator;
	private ViewGroup parentView;
	private List<PredefinedViewPendingAttributes> predefinedViewPendingAttributesGroup;
	
	public BinderImplementorImpl(Context context, BindingContextCreator bindingContextCreator)
	{
		this.context = context;
		this.bindingContextCreator = bindingContextCreator;
		predefinedViewPendingAttributesGroup = Lists.newArrayList();
	}
	
	@Override
	public BinderImplementor attachToRoot(ViewGroup parentView)
	{
		this.parentView = parentView;
		return this;
	}
	
	public BinderImplementor setPredefinedViewPendingAttributesGroup(Collection<PredefinedViewPendingAttributes> predefinedViewPendingAttributesGroup)
	{
		this.predefinedViewPendingAttributesGroup = Lists.newArrayList(predefinedViewPendingAttributesGroup);
		return this;
	}
	
	@Override
	public View bind(int layoutId, Object presentationModel)
	{
		InflatedView inflatedView = inflateView(layoutId);
		
		BindingContext context = bindingContextCreator.create(presentationModel);
		inflatedView.bindChildViews(context);
		
		return inflatedView.getRootView();
	}

	private InflatedView inflateView(int layoutId)
	{
		LayoutInflater layoutInflater = LayoutInflater.from(context).cloneInContext(context);
		BindingViewInflater.Builder bindingViewInflaterBuilder = new BindingViewInflater.Builder(layoutInflater);
		bindingViewInflaterBuilder.setParentViewToAttach(parentView);
		bindingViewInflaterBuilder.setPredefinedViewPendingAttributesGroup(predefinedViewPendingAttributesGroup);
		BindingViewInflater bindingViewInflater = bindingViewInflaterBuilder.create();
		InflatedView inflatedView = bindingViewInflater.inflateView(layoutId);
		return inflatedView;
	}

	public static interface BindingContextCreator
	{
		BindingContext create(Object presentationModel);
	}
}
