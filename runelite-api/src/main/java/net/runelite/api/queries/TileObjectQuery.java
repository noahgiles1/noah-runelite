package net.runelite.api.queries;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.Predicate;

import net.runelite.api.Client;
import net.runelite.api.Constants;
import net.runelite.api.Scene;
import net.runelite.api.Tile;
import net.runelite.api.TileObject;

public abstract class TileObjectQuery<EntityType extends TileObject, QueryType> extends LocatableQuery<EntityType, QueryType>
{
	protected List<Tile> getTiles(Client client)
	{
		List<Tile> tilesList = new ArrayList<>();
		Scene scene = client.getScene();
		Tile[][][] tiles = scene.getTiles();
		int z = client.getPlane();
		for (int x = 0; x < Constants.SCENE_SIZE; ++x)
		{
			for (int y = 0; y < Constants.SCENE_SIZE; ++y)
			{
				Tile tile = tiles[z][x][y];
				if (tile == null)
				{
					continue;
				}
				tilesList.add(tile);
			}
		}
		return tilesList;
	}

	@SuppressWarnings("unchecked")
	public QueryType idEquals(int... ids)
	{
		predicate = and(object ->
		{
			for (int id : ids)
			{
				if (object.getId() == id)
				{
					return true;
				}
			}
			return false;
		});
		return (QueryType) this;
	}

	public QueryType nameEquals(String... names)
	{
		predicate = and(object ->
		{
			for (String name : names)
			{
				String objectName = object.getName();
				if (objectName != null && objectName.equals(name))
				{
					return true;
				}
			}
			return false;
		});
		return (QueryType) this;
	}

	public QueryType actionEquals(String... actions)
	{
		predicate = and(object ->
		{
			for (String action : actions)
			{
				String[] objectActions = object.getActions();
				for (String objectAction : objectActions)
				{
					if (objectAction != null && objectAction.equals(action))
					{
						return true;
					}
				}
			}
			return false;
		});
		return (QueryType) this;
	}

	@SuppressWarnings("unchecked")
	public QueryType idEquals(Collection<Integer> ids)
	{
		predicate = and((object) -> ids.contains(object.getId()));
		return (QueryType) this;
	}

	@SuppressWarnings("unchecked")
	public QueryType filter(Predicate<EntityType> other)
	{
		predicate = and(other);
		return (QueryType) this;
	}
}