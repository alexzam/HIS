Ext.define('alexzam.his.comp.ChartPlugin', {
    // Solution proposed here:
    // http://www.sencha.com/forum/showthread.php?140791-Dynamically-add-series-to-a-chart&p=641673&viewfull=1#post641673

    chart: null,
    pluginId:'series',

    init:function(chart) {
        this.chart = chart;
    },

    // removes the serie 'serieId' from the chart 'chart'
    //  parameters:    chart        the chart object
    //                seriesId    the ID of the serie
    removeSerieById:function(seriesId) {
        var me = this;
        var surface = me.chart.surface;

        // get the key of the serie
        for (var serieKey = 0; serieKey < me.chart.series.keys.length; serieKey++) {
            // check for the searched serie
            if (me.chart.series.keys[serieKey] == seriesId) {
                // go through all the groups of the surface
                for (var groupKey = 0; groupKey < surface.groups.keys.length; groupKey++) {
                    // check if the group name contains the serie name
                    if (surface.groups.keys[groupKey].search(seriesId) == 0) {
                        // destroy the group
                        surface.groups.items[groupKey].destroy();
                    }
                }

                // get the correct serie
                var serie = me.chart.series.items[serieKey];

                // remove the serie from the chart
                me.chart.series.remove(serie);
            }
        }
    },

    removeAllSeries:function() {
        var me = this;

        var serkeys = Ext.Array.clone(me.chart.series.keys);
        Ext.each(serkeys, function(key) {
            me.removeSerieById(key);
        });

        // redraw the chart
        me.chart.redraw();
    }
});
