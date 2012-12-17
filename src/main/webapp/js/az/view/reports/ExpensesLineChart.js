Ext.define('alexzam.his.view.reports.ExpensesLineChart', {
    extend:'Ext.panel.Panel',

    alias:'widget.his.chart.expense',

    requires:[
        'alexzam.his.comp.ChartPlugin',
        'alexzam.his.model.reports.DateVal',
        'alexzam.his.model.account.store.Category',
        'alexzam.his.model.account.proxy.Category',
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

                    plugins:[Ext.create('alexzam.his.comp.ChartPlugin')],

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

                    series: [{
                         type: 'area',
                         axis: 'left',
                         xField: 'date',
                         yField: [],
                         highlight: true,
                         title:'Opa!',
                         markerConfig: {
                             type: 'circle',
                             size: 4,
                             radius: 4,
                             'stroke-width': 1
                         }
                     }],
                    legend: {
                        position: 'right'
                    }
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
                            this.ownerCt.fireEvent('filterchange');
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
                            this.ownerCt.fireEvent('filterchange');
                        }
                    }
                },
                {
                    xtype:'combo',
                    itemId:'cmbCat',
                    fieldLabel:'Категория',
                    name:'cat',
                    queryMode:'local',
                    valueField:'id',
                    displayField:'name',
                    lastQuery:'',
                    labelWidth:65,
                    multiSelect:true,
                    listeners:{
                        change:function() {
                            this.ownerCt.fireEvent('filterchange');
                        }
                    }
                }
            ]
        }
    ],

    initComponent:function () {
        var me = this;
        var chart = me.items[0].items[0];
        var catCombo = me.items[1].items[2];

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

            inManualRefresh:false,

            listeners:{
                datachanged:{
                    scope:me,
                    fn:function(store) {
                        var me = this;

                        if(store.inManualRefresh) return;
                        store.inManualRefresh = true;

                        var data = store.getProxy().getReader().rawData;
//                        me.chart.getPlugin('series').removeAllSeries();
                        me.chart.store.setValueFields(
                                Ext.Array.map(data.series, function(ser) {
                                    return ser.field;
                                })
                                );

                        me.chart.series.items[0].yField = [];
                        me.chart.axes.items[1].fields = [];
                        Ext.each(data.series, function(serie) {
//                            me.chart.series.add({
//                                type: 'line',
//                                axis: 'left',
//                                xField: 'date',
//                                yField: serie.field,
//                                highlight: true,
//                                markerConfig: {
//                                    type: 'circle',
//                                    size: 4,
//                                    radius: 4,
//                                    'stroke-width': 1
//                                }
//                            });
                            me.chart.series.items[0].yField.push(serie.field);
                            me.chart.axes.items[1].fields.push(serie.field);
                        });
                        me.chart.redraw(true);
                        store.inManualRefresh = false;
                    }
                }
            },
            dField:{name: 'date', type: 'date', dateFormat: 'd.m.Y'},

            setValueFields:function(fields) {
                var me = this;

                var fld = [new Ext.data.Field(me.dField)];
                Ext.each(fields, function(f) {
                    fld.push(new Ext.data.Field({
                        name: f,
                        type:'float'
                    }));
                }, me);
                me.model.setFields(fld);

                me.loadData(me.proxy.reader.rawData.items);
            }
        });

        catCombo.store = Ext.create('alexzam.his.model.account.store.Category',
        {
            proxy:Ext.create('alexzam.his.model.account.proxy.Category')
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
    }
});