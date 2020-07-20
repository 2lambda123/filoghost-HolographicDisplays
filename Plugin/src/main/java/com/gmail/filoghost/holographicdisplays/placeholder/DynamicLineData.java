/*
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program. If not, see <https://www.gnu.org/licenses/>.
 */
package com.gmail.filoghost.holographicdisplays.placeholder;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.gmail.filoghost.holographicdisplays.nms.interfaces.entity.NMSNameable;
import com.gmail.filoghost.holographicdisplays.util.Validator;

public class DynamicLineData {
	
	private final NMSNameable entity;
	private final String originalName;
	
	private Set<Placeholder> placeholders;
	private Set<PatternPlaceholder> patternPlaceholders;
	private final Map<String, Placeholder> animations;
	
	public DynamicLineData(NMSNameable entity, String originalName) {
		Validator.notNull(entity, "entity");
		
		this.entity = entity;
		this.originalName = originalName;
		placeholders = new HashSet<>();
		patternPlaceholders = new HashSet<>();
		animations = new HashMap<>();
	}

	public NMSNameable getEntity() {
		return entity;
	}

	public String getOriginalName() {
		return originalName;
	}
	
	public void setPlaceholders(Set<Placeholder> placeholders) {
		this.placeholders = placeholders;
	}

	public void setPatternPlaceholders(Set<PatternPlaceholder> patternPlaceholders) {
		this.patternPlaceholders = patternPlaceholders;
	}

	public Set<Placeholder> getPlaceholders() {
		return placeholders;
	}

	public Set<PatternPlaceholder> getPatternPlaceholders() {
		return patternPlaceholders;
	}

	public Map<String, Placeholder> getAnimations() {
		return animations;
	}

	@Override
	public int hashCode() {
		return entity.hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		DynamicLineData other = (DynamicLineData) obj;
		return this.entity == other.entity;
	}
	
	
	
}
