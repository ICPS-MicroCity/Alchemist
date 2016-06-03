/*
 * Copyright (C) 2010-2016, Danilo Pianini and contributors

 * listed in the project's pom.xml file.
 * 
 * This file is part of Alchemist, and is distributed under the terms of
 * the GNU General Public License, with a linking exception, as described
 * in the file LICENSE in the Alchemist distribution's top directory.
 */
package it.unibo.alchemist.model.implementations.nodes;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.google.common.collect.MapMaker;

import it.unibo.alchemist.model.implementations.molecules.Junction;
import it.unibo.alchemist.model.interfaces.Environment;
import it.unibo.alchemist.model.interfaces.ICellNode;
import it.unibo.alchemist.model.interfaces.Molecule;

/**
 *
 */
public class CellNode extends DoubleNode implements ICellNode {

    private static final long serialVersionUID = 837704874534888283L;

    private final Map<Junction, Map<ICellNode, Integer>> junctions = new MapMaker().concurrencyLevel(2).makeMap();

    /**
     * create a new cell node.
     * @param env the environment
     */
    public CellNode(final Environment<Double> env) {
        super(env);
    }

    @Override
    protected Double createT() {
        return 0d;
    }

    @Override
    public void setConcentration(final Molecule mol, final Double c) {
        if (c > 0) {
            super.setConcentration(mol, c);
        } else {
            removeConcentration(mol);
        }
    }

    @Override
    public Map<Junction, Map<ICellNode, Integer>> getJunctions() {
        return Collections.unmodifiableMap(junctions);
    }

    @Override
    public void addJunction(final Junction j, final ICellNode neighbor) {
        if (junctions.containsKey(j)) {
            final Map<ICellNode, Integer> inner = junctions.get(j);
            if (inner.containsKey(neighbor)) {
                inner.put(neighbor, inner.get(neighbor) + 1);
            } else {
                inner.put(neighbor, 1);
            }
            junctions.put(j, inner);
        } else {
            final Map<ICellNode, Integer> tmp = new HashMap<>(1);
            tmp.put(neighbor, 1);
            junctions.put(j, tmp);
        }
    }

    @Override
    public boolean containsJunction(final Junction j) {
        return junctions.containsKey(j);
    }

    @Override
    public void removeJunction(final Junction j, final ICellNode neighbor) {
//        final Iterator<Junction> it = junctionList.iterator();
//        for (int i = 0; it.hasNext(); i++) {
//            final Junction jun = it.next();
//            if (jun.equals(j) && jun.getNeighborNode().get().equals(neighbor)) {
//                junctionList.remove(i);
//                return;
//            }
//        }
        if (junctions.containsKey(j)) {
            final Map<ICellNode, Integer> inner = junctions.get(j);
            if (inner.containsKey(neighbor)) {
                if (inner.get(neighbor) == 1) { // only one junction j with neighbor
                    inner.remove(neighbor);
                } else {
                    inner.put(neighbor, inner.get(neighbor) - 1);
                }
                junctions.put(j, inner);
            }
        }
    }

    @Override
    public Set<ICellNode> getNeighborsLinkWithJunction(final Junction j) {
        if (junctions.get(j) == null) {
            return Collections.emptySet();
        }
        return new HashSet<>(junctions.get(j).keySet());
    }

    @Override
    public int getJunctionNumber() {
        return junctions.values().stream().mapToInt(m -> m.values().stream().mapToInt(v -> v.intValue()).sum()).sum();
    }

}
