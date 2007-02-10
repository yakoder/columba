// The contents of this file are subject to the Mozilla Public License Version
// 1.1
//(the "License"); you may not use this file except in compliance with the
//License. You may obtain a copy of the License at http://www.mozilla.org/MPL/
//
//Software distributed under the License is distributed on an "AS IS" basis,
//WITHOUT WARRANTY OF ANY KIND, either express or implied. See the License
//for the specific language governing rights and
//limitations under the License.
//
//The Original Code is "The Columba Project"
//
//The Initial Developers of the Original Code are Frederik Dietz and Timo
// Stich.
//Portions created by Frederik Dietz and Timo Stich are Copyright (C) 2003.
//
//All Rights Reserved.
package org.columba.calendar.store;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.Vector;

import org.columba.calendar.base.UUIDGenerator;
import org.columba.calendar.model.ComponentInfoList;
import org.columba.calendar.model.DateRange;
import org.columba.calendar.model.api.IComponent;
import org.columba.calendar.model.api.IComponentInfo;
import org.columba.calendar.model.api.IComponentInfoList;
import org.columba.calendar.model.api.IDateRange;
import org.columba.calendar.model.api.IEvent;
import org.columba.calendar.model.api.IEventInfo;
import org.columba.calendar.model.api.ITodo;
import org.columba.calendar.parser.SyntaxException;
import org.columba.calendar.parser.VCalendarModelFactory;
import org.columba.calendar.store.api.ICalendarStore;
import org.columba.calendar.store.api.StoreException;
import org.columba.core.io.DiskIO;
import org.jdom.Document;

public class LocalCalendarStore extends AbstractCalendarStore implements
		ICalendarStore {

	private LocalXMLFileStore dataStorage;

	public LocalCalendarStore(File directory) throws StoreException {
		super();

		if (directory == null)
			throw new IllegalArgumentException("directory == null");

		DiskIO.ensureDirectory(directory);

		dataStorage = new LocalXMLFileStore(directory);
	}

	/**
	 * @see org.columba.calendar.store.AbstractCalendarStore#add(org.columba.calendar.model.api.IComponent)
	 */
	public void add(IComponentInfo basicModel) throws StoreException {

		if (basicModel == null)
			throw new IllegalArgumentException("basicModel == null");

		String id = basicModel.getId();
		// generate new UUID if it does not exist yet
		if (id == null)
			id = new UUIDGenerator().newUUID();

		Document document = null;
		try {
			document = VCalendarModelFactory.marshall(basicModel);
		} catch (SyntaxException e) {
			throw new StoreException(e);
		} catch (IllegalArgumentException e) {
			throw new StoreException(e);
		}

		dataStorage.save(id, document);

		fireItemAdded(id);

	}

	/**
	 * @see org.columba.calendar.store.AbstractCalendarStore#exists(java.lang.Object)
	 */
	public boolean exists(Object id) throws StoreException {

		if (id == null)
			throw new IllegalArgumentException("id == null");

		return dataStorage.exists(id);
	}

	/**
	 * @see org.columba.calendar.store.AbstractCalendarStore#get(java.lang.Object)
	 */
	public IComponentInfo get(Object id) throws StoreException {

		if (id == null)
			throw new IllegalArgumentException("id == null");

		Document document = dataStorage.load(id);
		if (document == null)
			throw new StoreException("document == null, id=" + id);

		IComponentInfo basicModel = null;
		try {
			basicModel = VCalendarModelFactory.unmarshall(document);
		} catch (SyntaxException e) {
			throw new StoreException(e);
		} catch (IllegalArgumentException e) {
			throw new StoreException(e);
		}

		return basicModel;
	}

	/**
	 * @see org.columba.calendar.store.AbstractCalendarStore#modify(java.lang.Object,
	 *      org.columba.calendar.model.api.IComponent)
	 */
	public void modify(Object id, IComponentInfo basicModel) throws StoreException {
		if (id == null)
			throw new IllegalArgumentException("id == null");

		if (basicModel == null)
			throw new IllegalArgumentException("basicModel == null");

		// remove old data
		dataStorage.remove(id);

		// generate xml document
		Document document = null;
		try {
			document = VCalendarModelFactory.marshall(basicModel);
		} catch (SyntaxException e) {
			throw new StoreException(e);
		} catch (IllegalArgumentException e) {
			throw new StoreException(e);
		}

		// add new data to local store
		dataStorage.save(id, document);

		super.modify(id, basicModel);
	}

	/**
	 * @see org.columba.calendar.store.AbstractCalendarStore#remove(java.lang.Object)
	 */
	public void remove(Object id) throws StoreException {
		if (id == null)
			throw new IllegalArgumentException("id == null");

		dataStorage.remove(id);

		super.remove(id);
	}

	/**
	 * @see org.columba.calendar.store.AbstractCalendarStore#getComponentInfoList()
	 */
	public IComponentInfoList getComponentInfoList() throws StoreException {

		IComponentInfoList list = new ComponentInfoList();

		Iterator it = dataStorage.iterator();
		while (it.hasNext()) {
			Document document = (Document) it.next();

			IComponentInfo basicModel = null;
			try {
				basicModel = VCalendarModelFactory.unmarshall(document);
			} catch (SyntaxException e) {
				throw new StoreException(e);
			} catch (IllegalArgumentException e) {
				throw new StoreException(e);
			}

			if (basicModel.getType() == IComponent.TYPE.EVENT) {
				IEventInfo event = (IEventInfo) basicModel;
				list.add(event);
			}
		}

		return list;
	}

	public IComponentInfoList getComponentInfoList(String calendarId)
			throws StoreException {
		IComponentInfoList list = new ComponentInfoList();

		Iterator it = dataStorage.iterator();
		while (it.hasNext()) {
			Document document = (Document) it.next();

			IComponentInfo basicModel = null;
			try {
				basicModel = VCalendarModelFactory.unmarshall(document);
			} catch (SyntaxException e) {
				throw new StoreException(e);
			} catch (IllegalArgumentException e) {
				throw new StoreException(e);
			}

			if (basicModel.getType() == IComponent.TYPE.EVENT) {
				IEventInfo event = (IEventInfo) basicModel;
				if (event.getCalendar().equals(calendarId)) {
					list.add(event);
				}
			}
		}

		return list;
	}

	public Iterator<String> getIdIterator() throws StoreException {
		ArrayList<String> result = new ArrayList<String>();

		Iterator it = dataStorage.iterator();
		while (it.hasNext()) {
			Document document = (Document) it.next();

			IComponentInfo basicModel = null;
			try {
				basicModel = VCalendarModelFactory.unmarshall(document);
			} catch (SyntaxException e) {
				throw new StoreException(e);
			} catch (IllegalArgumentException e) {
				throw new StoreException(e);
			}

			if (basicModel.getType() == IComponent.TYPE.EVENT) {
				IEventInfo event = (IEventInfo) basicModel;
				result.add(event.getId());
			}
		}

		return result.iterator();
	}

	public Iterator<String> getIdIterator(String calendarId)
			throws StoreException {
		ArrayList<String> result = new ArrayList<String>();

		Iterator it = dataStorage.iterator();
		while (it.hasNext()) {
			Document document = (Document) it.next();

			IComponentInfo basicModel = null;
			try {
				basicModel = VCalendarModelFactory.unmarshall(document);
			} catch (SyntaxException e) {
				throw new StoreException(e);
			} catch (IllegalArgumentException e) {
				throw new StoreException(e);
			}

			if (basicModel.getType() == IComponent.TYPE.EVENT) {
				IEventInfo event = (IEventInfo) basicModel;
				if (event.getCalendar().equals(calendarId))
					result.add(event.getId());
			}
		}

		return result.iterator();
	}

	/**
	 * *********************** very slow search implementation
	 * **********************
	 */

	/**
	 * @see org.columba.calendar.store.api.ICalendarStore#findByDateRange(org.columba.calendar.model.api.IDateRange)
	 */
	public Iterator<String> findByDateRange(IDateRange dateRange)
			throws StoreException {
		Vector<String> result = new Vector<String>();

		Iterator<String> it = getIdIterator();
		while (it.hasNext()) {
			String id = it.next();
			IComponentInfo c = get(id);
			if (c.getType().equals(IComponent.TYPE.EVENT)) {
				IEvent event = (IEvent) c;
				Calendar startDate = event.getDtStart();
				Calendar endDate = event.getDtEnd();
				IDateRange dr = new DateRange(startDate, endDate);
				if (dateRange.equals(dr))
					result.add(id);
			} else if (c.getType().equals(IComponent.TYPE.TODO)) {
				ITodo todo = (ITodo) c;
				Calendar startDate = todo.getDtStart();
				Calendar endDate = todo.getDue();
				IDateRange dr = new DateRange(startDate, endDate);
				if (dateRange.equals(dr))
					result.add(id);
			} else
				throw new IllegalArgumentException(
						"unsupported component type " + c.getType());
		}

		return result.listIterator();
	}

	/**
	 * @see org.columba.calendar.store.api.ICalendarStore#findByStartDate(java.util.Calendar)
	 */
	public Iterator<String> findByStartDate(Calendar startDate)
			throws StoreException {
		Vector<String> result = new Vector<String>();

		Iterator<String> it = getIdIterator();
		while (it.hasNext()) {
			String id = it.next();
			IComponentInfo c = get(id);
			if (c.getType().equals(IComponent.TYPE.EVENT)) {
				IEvent event = (IEvent) c;
				Calendar sd = event.getDtStart();
				if (startDate.equals(sd))
					result.add(id);
			} else if (c.getType().equals(IComponent.TYPE.TODO)) {
				ITodo todo = (ITodo) c;
				Calendar sd = todo.getDtStart();
				Calendar endDate = todo.getDue();
				IDateRange dr = new DateRange(startDate, endDate);
				if (startDate.equals(sd))
					result.add(id);
			} else
				throw new IllegalArgumentException(
						"unsupported component type " + c.getType());
		}

		return result.listIterator();
	}

	/**
	 * @see org.columba.calendar.store.api.ICalendarStore#findBySummary(java.lang.String)
	 */
	public Iterator<String> findBySummary(String searchTerm)
			throws StoreException {
		Vector<String> result = new Vector<String>();

		Iterator<String> it = getIdIterator();
		while (it.hasNext()) {
			String id = it.next();
			IComponentInfo c = get(id);
			if (c.getType().equals(IComponent.TYPE.EVENT)) {
				IEventInfo event = (IEventInfo) c;
				String summary = event.getEvent().getSummary();
				if (summary.indexOf(searchTerm) != -1)
					result.add(id);
			} else if (c.getType().equals(IComponent.TYPE.TODO)) {
				ITodo todo = (ITodo) c;
				String summary = todo.getSummary();
				if (summary.indexOf(searchTerm) != -1)
					result.add(id);
			} else
				throw new IllegalArgumentException(
						"unsupported component type " + c.getType());
		}

		return result.listIterator();
	}

}
