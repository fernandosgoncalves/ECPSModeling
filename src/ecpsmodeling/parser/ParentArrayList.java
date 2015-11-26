package ecpsmodeling.parser;

import java.util.ArrayList;

public class ParentArrayList extends ArrayList<Object> {
	private static final long serialVersionUID = 1L;
	
	public static int FIRST_CHILD = 4;
	public static int NO_CHILD = 2;
	public static int DIRECT = 0;
	
	public ParentArrayList parent;
	public String name;
	public String value;
	
	public ParentArrayList() {
		super();
		this.parent = null;
	}

	public ParentArrayList(ParentArrayList parent, String name, String value) {
		super();
		this.parent = parent;
		this.name = name;
		this.value = value;
	}

	public ParentArrayList(ParentArrayList parent, String name) {
		super();
		this.parent = parent;
		this.name = name;
	}

	// returna nulo para não dar erros de null pointer exception caso o tamanho
	// seja 0.
	@Override
	public ParentArrayList get(int arg0) {
		if (arg0 >= this.size())
			return null;
		return (ParentArrayList) super.get(arg0);
	}

	private ParentArrayList goDown(ParentArrayList ret, ParentArrayList list, String name, ArrayList<String> value,
			String parent, int level, int limit) {
		level++;
		for (int i = 0; i < list.size(); i++) {
			if (parent == null && value == null) {
				if (list.get(i).name.equals(name))
					ret.add(list.get(i));
				// ver se dá pra melhorar...
				// Porque chamar recursivo? e não deixar apenas o for? Qual a
				// função do for fora da recursão?
				if (level <= limit)
					goDown(ret, list.get(i), name, value, parent, level, limit);
			}
			if (parent == null && value != null) {
				for (int j = 0; j < value.size(); j++) {
					if (list.get(i).name.equals(name) && list.get(i).value.equals(value.get(j)))
						ret.add(list.get(i));
				}
				// ver se dá pra melhorar...
				if (level <= limit)
					goDown(ret, list.get(i), name, value, parent, level, limit);
			}
			if (value != null && parent != null) {
				if (list.get(i).name.equals(name) && list.get(i).value.equals(value)
						&& list.get(i).parent.name.equals(parent))
					ret.add(list.get(i));
				// ver se dá pra melhorar...
				if (level <= limit)
					goDown(ret, list.get(i), name, value, parent, level, limit);
			}
		}
		level--;
		return null;
	}

	public ParentArrayList getByName(String name, ArrayList<String> value, String parent, int limit) {
		int level = 1;
		ParentArrayList ret = new ParentArrayList();
		goDown(ret, this, name, value, parent, level, limit);
		return ret;
	}

	public ParentArrayList getByName(String name, ArrayList<String> value, int limit) {
		return getByName(name, value, null, limit);
	}

	public ParentArrayList getByName(String name, int limit) {
		return getByName(name, null, limit);
	}

	public ParentArrayList getByName(String name) {
		return getByName(name, null, FIRST_CHILD);
	}
}
