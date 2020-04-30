/**
 * Feature2DStyle is part of the OrbisGIS platform
 * 
 * OrbisGIS is a java GIS application dedicated to research in GIScience.
 * OrbisGIS is developed by the GIS group of the DECIDE team of the
 * Lab-STICC CNRS laboratory, see <http://www.lab-sticc.fr/>.
 *
 * The GIS group of the DECIDE team is located at :
 *
 * Laboratoire Lab-STICC – CNRS UMR 6285 Equipe DECIDE UNIVERSITÉ DE
 * BRETAGNE-SUD Institut Universitaire de Technologie de Vannes 8, Rue Montaigne
 * - BP 561 56017 Vannes Cedex
 *
 * Feature2DStyle is distributed under LGPL 3 license.
 *
 * Copyright (C) 2007-2014 CNRS (IRSTV FR CNRS 2488)
 * Copyright (C) 2015-2020 CNRS (Lab-STICC UMR CNRS 6285)
 *
 *
 * Feature2DStyle is free software: you can redistribute it and/or modify it under the
 * terms of the GNU Lesser General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at your option) any later
 * version.
 *
 * Feature2DStyle is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR
 * A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License along with
 * Feature2DStyle. If not, see <http://www.gnu.org/licenses/>.
 *
 * For more information, please consult: <http://www.orbisgis.org/>
 * or contact directly: info_at_ orbisgis.org
 */
package org.orbisgis.orbismap.style.graphic.shapeFactory;

import java.util.HashMap;

/**
 * Weather mark factory
 * @author Erwan Bocher (CNRS)
 *
 * This WeatherMarkFactory is based on the TTF file created and maintained by
 * Erik Flowers.
 *
 * See : https://erikflowers.github.io/weather-icons/
 *
 */
public class WeatherMarkFactory extends AbstractTTFMarkFactory {

    public static final String FACTORY_PREFIX = "weather";

    HashMap<String, String> humanShapeName;

    public WeatherMarkFactory() {
        super(FACTORY_PREFIX, "weathericons-regular-webfont.ttf");
        initHumanShapeNames();
    }

    @Override
    public String getShapeName() {
        if (humanShapeName.containsKey(shapeName.toLowerCase())) {
            return humanShapeName.get(shapeName.toLowerCase());
        }
        return shapeName;
    }

    private void initHumanShapeNames() {
        humanShapeName = new HashMap<>();
        humanShapeName.put("wind-beaufort-0", "f0b7");
        humanShapeName.put("wind-beaufort-1", "f0b8");
        humanShapeName.put("wind-beaufort-2", "f0b9");
        humanShapeName.put("wind-beaufort-3", "f0ba");
        humanShapeName.put("wind-beaufort-4", "f0bb");
        humanShapeName.put("wind-beaufort-5", "f0bc");
        humanShapeName.put("wind-beaufort-6", "f0bd");
        humanShapeName.put("wind-beaufort-7", "f0be");
        humanShapeName.put("wind-beaufort-8", "f0bf");
        humanShapeName.put("wind-beaufort-9", "f0c0");
        humanShapeName.put("wind-beaufort-10", "f0c1");
        humanShapeName.put("wind-beaufort-11", "f0c2");
        humanShapeName.put("wind-beaufort-12", "f0c3");
        humanShapeName.put("day-sunny", "f00d");
        humanShapeName.put("day-cloudy", "f002");
        humanShapeName.put("day-cloudy-gusts", "f000");
        humanShapeName.put("day-cloudy-windy", "f001");
        humanShapeName.put("day-fog", "f003");
        humanShapeName.put("day-hail", "f004");
        humanShapeName.put("day-haze", "f0b6");
        humanShapeName.put("day-lightning", "f005");
        humanShapeName.put("day-rain", "f008");
        humanShapeName.put("day-rain-mix", "f006");
        humanShapeName.put("day-rain-wind", "f007");
        humanShapeName.put("day-showers", "f009");
        humanShapeName.put("day-sleet", "f0b2");
        humanShapeName.put("day-sleet-storm", "f068");
        humanShapeName.put("day-snow", "f00a");
        humanShapeName.put("day-snow-thunderstorm", "f06b");
        humanShapeName.put("day-snow-wind", "f065");
        humanShapeName.put("day-sprinkle", "f00b");
        humanShapeName.put("day-storm-showers", "f00e");
        humanShapeName.put("day-sunny-overcast", "f00c");
        humanShapeName.put("day-thunderstorm", "f010");
        humanShapeName.put("day-windy", "f085");
        humanShapeName.put("solar-eclipse", "f06e");
        humanShapeName.put("hot", "f072");
        humanShapeName.put("day-cloudy-high", "f07d");
        humanShapeName.put("day-light-wind", "f0c4");
        humanShapeName.put("direction-up", "f058");
        humanShapeName.put("direction-up-right", "f057");
        humanShapeName.put("direction-right", "f04d");
        humanShapeName.put("direction-down-right", "f088");
        humanShapeName.put("direction-down", "f044");
        humanShapeName.put("direction-down-left", "f043");
        humanShapeName.put("direction-left", "f048");
        humanShapeName.put("direction-up-left", "f087");
        humanShapeName.put("alien", "f075");
        humanShapeName.put("celsius", "f03c");
        humanShapeName.put("fahrenheit", "f045");
        humanShapeName.put("degrees", "f042");
        humanShapeName.put("thermometer", "f055");
        humanShapeName.put("thermometer-exterior", "f053");
        humanShapeName.put("thermometer-internal", "f054");
        humanShapeName.put("cloud-down", "f03d");
        humanShapeName.put("cloud-up", "f040");
        humanShapeName.put("cloud-refresh", "f03e");
        humanShapeName.put("horizon", "f047");
        humanShapeName.put("horizon-alt", "f046");
        humanShapeName.put("sunrise", "f051");
        humanShapeName.put("sunset", "f052");
        humanShapeName.put("moonrise", "f0c9");
        humanShapeName.put("moonset", "f0ca");
        humanShapeName.put("refresh", "f04c");
        humanShapeName.put("refresh-alt", "f04b");
        humanShapeName.put("umbrella", "f084");
        humanShapeName.put("barometer", "f079");
        humanShapeName.put("humidity", "f07a");
        humanShapeName.put("na", "f07b");
        humanShapeName.put("train", "f0cb");
        humanShapeName.put("cloud", "f041");
        humanShapeName.put("cloudy", "f013");
        humanShapeName.put("cloudy-gusts", "f011");
        humanShapeName.put("cloudy-windy", "f012");
        humanShapeName.put("fog", "f014");
        humanShapeName.put("hail", "f015");
        humanShapeName.put("rain", "f019");
        humanShapeName.put("rain-mix", "f017");
        humanShapeName.put("rain-wind", "f018");
        humanShapeName.put("showers", "f01a");
        humanShapeName.put("sleet", "f0b5");
        humanShapeName.put("snow", "f01b");
        humanShapeName.put("sprinkle", "f01c");
        humanShapeName.put("storm-showers", "f01d");
        humanShapeName.put("thunderstorm", "f01e");
        humanShapeName.put("snow-wind", "f064");
        humanShapeName.put("snow", "f01b");
        humanShapeName.put("smog", "f074");
        humanShapeName.put("smoke", "f062");
        humanShapeName.put("lightning", "f016");
        humanShapeName.put("raindrops", "f04e");
        humanShapeName.put("raindrop", "f078");
        humanShapeName.put("dust", "f063");
        humanShapeName.put("snowflake-cold", "f076");
        humanShapeName.put("windy", "f021");
        humanShapeName.put("strong-wind", "f050");
        humanShapeName.put("sandstorm", "f082");
        humanShapeName.put("earthquake", "f0c6");
        humanShapeName.put("fire", "f0c7");
        humanShapeName.put("flood", "f07c");
        humanShapeName.put("meteor", "f071");
        humanShapeName.put("tsunami", "f0c5");
        humanShapeName.put("volcano", "f0c8");
        humanShapeName.put("hurricane", "f073");
        humanShapeName.put("tornado", "f056");
        humanShapeName.put("small-craft-advisory", "f0cc");
        humanShapeName.put("gale-warning", "f0cd");
        humanShapeName.put("storm-warning", "f0ce");
        humanShapeName.put("hurricane-warning", "f0cf");
        humanShapeName.put("wind-direction", "f0b1");
        humanShapeName.put("night-clear", "f02e");
        humanShapeName.put("night-alt-cloudy", "f086");
        humanShapeName.put("night-alt-cloudy-gusts", "f022");
        humanShapeName.put("night-alt-cloudy-windy", "f023");
        humanShapeName.put("night-alt-hail", "f024");
        humanShapeName.put("night-alt-lightning", "f025");
        humanShapeName.put("night-alt-rain", "f028");
        humanShapeName.put("night-alt-rain-mix", "f026");
        humanShapeName.put("night-alt-rain-wind", "f027");
        humanShapeName.put("night-alt-showers", "f029");
        humanShapeName.put("night-alt-sleet", "f0b4");
        humanShapeName.put("night-alt-sleet-storm", "f06a");
        humanShapeName.put("night-alt-snow", "f02a");
        humanShapeName.put("night-alt-snow-thunderstorm", "f06d");
        humanShapeName.put("night-alt-snow-wind", "f067");
        humanShapeName.put("night-alt-sprinkle", "f02b");
        humanShapeName.put("night-alt-storm-showers", "f02c");
        humanShapeName.put("night-alt-thunderstorm", "f02d");
        humanShapeName.put("night-cloudy", "f031");
        humanShapeName.put("night-cloudy-gusts", "f02f");
        humanShapeName.put("night-cloudy-windy", "f030");
        humanShapeName.put("night-fog", "f04a");
        humanShapeName.put("night-hail", "f032");
        humanShapeName.put("night-lightning", "f033");
        humanShapeName.put("night-partly-cloudy", "f083");
        humanShapeName.put("night-rain", "f036");
        humanShapeName.put("night-rain-mix", "f034");
        humanShapeName.put("night-rain-wind", "f035");
        humanShapeName.put("night-showers", "f037");
        humanShapeName.put("night-sleet", "f0b3");
        humanShapeName.put("night-sleet-storm", "f069");
        humanShapeName.put("night-snow", "f038");
        humanShapeName.put("night-snow-thunderstorm", "f06c");
        humanShapeName.put("night-snow-wind", "f066");
        humanShapeName.put("night-sprinkle", "f039");
        humanShapeName.put("night-storm-showers", "f03a");
        humanShapeName.put("night-thunderstorm", "f03b");
        humanShapeName.put("lunar-eclipse", "f070");
        humanShapeName.put("stars", "f077");
        humanShapeName.put("storm-showers", "f01d");
        humanShapeName.put("thunderstorm", "f01e");
        humanShapeName.put("night-alt-cloudy-high", "f07e");
        humanShapeName.put("night-cloudy-high", "f080");
        humanShapeName.put("night-alt-partly-cloudy", "f081");
        humanShapeName.put("time-1", "f08a");
        humanShapeName.put("time-2", "f08b");
        humanShapeName.put("time-3", "f08c");
        humanShapeName.put("time-4", "f08d");
        humanShapeName.put("time-5", "f08e");
        humanShapeName.put("time-6", "f08f");
        humanShapeName.put("time-7", "f090");
        humanShapeName.put("time-8", "f091");
        humanShapeName.put("time-9", "f092");
        humanShapeName.put("time-10", "f093");
        humanShapeName.put("time-11", "f094");
        humanShapeName.put("time-12", "f089");
        humanShapeName.put("n", "f0b1");
        humanShapeName.put("nne", "f0b1");
        humanShapeName.put("ne", "f0b1");
        humanShapeName.put("ene", "f0b1");
        humanShapeName.put("e", "f0b1");
        humanShapeName.put("ese", "f0b1");
        humanShapeName.put("se", "f0b1");
        humanShapeName.put("sse", "f0b1");
        humanShapeName.put("s", "f0b1");
        humanShapeName.put("ssw", "f0b1");
        humanShapeName.put("sw", "f0b1");
        humanShapeName.put("wsw", "f0b1");
        humanShapeName.put("w", "f0b1");
        humanShapeName.put("wnw", "f0b1");
        humanShapeName.put("nw", "f0b1");
        humanShapeName.put("nnw", "f0b1");
        humanShapeName.put("n", "f0b1");
        humanShapeName.put("nne", "f0b1");
        humanShapeName.put("ne", "f0b1");
        humanShapeName.put("ene", "f0b1");
        humanShapeName.put("e", "f0b1");
        humanShapeName.put("ese", "f0b1");
        humanShapeName.put("se", "f0b1");
        humanShapeName.put("sse", "f0b1");
        humanShapeName.put("s", "f0b1");
        humanShapeName.put("ssw", "f0b1");
        humanShapeName.put("sw", "f0b1");
        humanShapeName.put("wsw", "f0b1");
        humanShapeName.put("w", "f0b1");
        humanShapeName.put("wnw", "f0b1");
        humanShapeName.put("nw", "f0b1");
        humanShapeName.put("nnw", "f0b1");
    }

}
