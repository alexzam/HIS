Ext.define('alexzam.his.view.reports.ExpensesLineChart', {
    extend:'Ext.panel.Panel',

    alias:'widget.his.chart.expense',

    requires:[
        'alexzam.his.model.reports.DateVal',
        'Ext.chart.theme.Base',
        'Ext.chart.axis.Numeric',
        'Ext.chart.axis.Time',
        'Ext.chart.series.Line',
        'Ext.data.Request',
        'Ext.data.Store',
        'Ext.data.reader.Json',
        'Ext.form.field.Date'
    ],

    layout:'border',

    items:[
        {
            xtype:'panel',
            itemId:'pnlChart',

            region:'center',

            layout:'fit',
            items:[
                {
                    xtype:'chart',
                    itemId:'chart',

                    store:null,
                    animate: true,
                    autoRender:true,

                    axes: [
                        {
                            type: 'Time',
                            position: 'bottom',
                            fields: ['date'],
                            title: 'Дата',
                            dateFormat: 'M j',
                            step: [Ext.Date.DAY, 1]
                        },
                        {
                            type: 'Numeric',
                            position: 'left',
                            fields: ['value'],
                            title: 'Сумма'
                        }
                    ],

                    series: []
                }
            ]
        },
        {
            xtype:'form',
            itemId:'filterForm',

            region:'east',

            bodyPadding:5,
            layout:{
                type:'vbox',
                align:'stretch',
                constrainAlign:true
            },
            bubbleEvents:['filterchange'],
            items:[
                {
                    xtype:'datefield',
                    itemId:'dtFrom',
                    fieldLabel:'С',
                    name:'from',
                    format:'d.m.Y',
                    allowBlank:false,
                    labelWidth:65,
                    startDay:1,
                    listeners:{
                        change:function() {
                            this.ownerCt.fireEvent('filterchange')
                        }
                    }
                },
                {
                    xtype:'datefield',
                    itemId:'dtTo',
                    fieldLabel:'По',
                    name:'to',
                    format:'d.m.Y',
                    allowBlank:false,
                    labelWidth:65,
                    startDay:1,
                    listeners:{
                        change:function() {
                            this.ownerCt.fireEvent('filterchange')
                        }
                    }
                }
            ]
        }
    ],

    initComponent:function () {
        var me = this;
        var chart = me.items[0].items[0];

        chart.store = Ext.create('Ext.data.Store',
        {
            model:'alexzam.his.model.reports.DateVal',
            proxy:{
                type:'ajax',
                url:Ext.rootUrl + 'reports/data/expenses',
                reader:{
                    type:'json',
                    root:'items'
                }
            },
            sorters:[
                {
                    property:'date'
                }
            ],
            listeners:{
                load:{
                    scope:me,
                    fn:function(store) {

                    }
                } ,
                datachanged:{
                    scope:me,
                    fn:function(store) {
                        var data = store.getProxy().getReader().rawData;
                        var me = this;
                        var serkeys = Ext.Array.clone(me.chart.series.keys);
                        Ext.each(serkeys, function(key) {
//                            Ext.each(ser.items, function(i){i.sprite.destroy()});
//                            ser.line.destroy();
//                            ser.destroy();
                            me.removeSerie(me.chart, key);
                        });
//                        me.chart.surface.removeAll(true);
//                        me.chart.series.clear();

//                        me.chart.axes.each(function(axis){
//                            axis.displaySprite = null;
//                        });
//
//                        me.chart.surface.destroy();
//                        me.chart.createSurface();
                        Ext.each(data.series, function(serie) {
                            me.chart.series.add({
                                type: 'line',
                                axis: 'left',
                                xField: 'date',
                                yField: serie.field,
                                highlight: true,
                                markerConfig: {
                                    type: 'circle',
                                    size: 4,
                                    radius: 4,
                                    'stroke-width': 1
                                }
                            });
                        });
                        me.chart.redraw(true);
                    }
                }
            }
        });

        me.callParent();

        me.chart = me.getComponent('pnlChart').getComponent('chart');
        me.filterForm = me.getComponent('filterForm');
        me.filterForm.dtTo = me.filterForm.getComponent('dtTo');
        me.filterForm.dtFrom = me.filterForm.getComponent('dtFrom');

        var d = new Date();
        d.setDate(1);
        me.filterForm.dtFrom.setValue(d);

        var d2 = Ext.Date.add(d, Ext.Date.MONTH, 1);
        d2 = Ext.Date.add(d2, Ext.Date.DAY, -1);
        me.filterForm.dtTo.setValue(d2);

        me.updateData();

        me.on('filterchange', me.filterchange);
    },

    updateData: function() {
        var me = this;
        var form = me.filterForm.getForm();
        if (!form.isValid()) return;

        var data = form.getFieldValues();

        data.from = data.from.getTime();
        data.to = data.to.getTime();

        me.chart.store.proxy.extraParams = data;
        me.chart.store.load();
    },

    filterchange:function() {
        var me = this;

        var form = me.filterForm;

        form.dtTo.setMinValue(form.dtFrom.getValue());
        form.dtFrom.setMaxValue(form.dtTo.getValue());

        me.updateData();
    },

    // removes the serie 'serieId' from the chart 'chart'
    //  parameters:    chart        the chart object
    //                seriesId    the ID of the serie
    removeSerie:function(chart, seriesId) {
        // get the surface
        var surface = chart.surface;

        // get the key of the serie
        for (var serieKey = 0; serieKey < chart.series.keys.length; serieKey++) {
            // check for the searched serie
            if (chart.series.keys[serieKey] == seriesId) {
                // go through all the groups of the surface
                for (var groupKey = 0; groupKey < surface.groups.keys.length; groupKey++) {
                    // check if the group name contains the serie name
                    if (surface.groups.keys[groupKey].search(seriesId) == 0) {
                        // destroy the group
                        surface.groups.items[groupKey].destroy();
                    }
                }


                // get the correct serie
                var serie = chart.series.items[serieKey];

                // remove the serie from the chart
                chart.series.remove(serie);


                // redraw the chart
                chart.redraw();
            }
        }
    }
});