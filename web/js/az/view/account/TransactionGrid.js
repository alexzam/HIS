Ext.define('alexzam.his.view.account.TransactionGrid', {
    extend:'Ext.grid.Panel',
    alias:'widget.his.account.TransactionGrid',

    requires:[
        'alexzam.his.model.account.store.Transaction',
        'alexzam.his.model.account.proxy.Transaction',
        'Ext.grid.column.Date',
        'Ext.grid.column.Number',
        'Ext.grid.feature.Summary',

        'Ext.form.field.Display'
    ],

    multiSelect:true,

    columns:[
        {
            header:'Когда',
            dataIndex:'timestamp',
            xtype:'datecolumn',
            format:'d.m.Y',
            editor:{
                xtype:'datefield',
                maxValue:new Date(),
                format:'d.m.Y',
                startDay:1
            }
        },
        {
            header:'Кто',
            dataIndex:'actor_id',
            editor:{
//                xclass:'Ext.form.field.ComboBox',
                xtype:'combo',
                displayField:'name',
                valueField:'id',
                queryMode: 'local',
                store:Ext.create('Ext.data.Store', {
                    fields: ['id', 'name'],
                    data : [
                        {id:1, name:"AlexZam"},
                        {id:2, name:"Anitra"}
                    ]
                })
            },
            renderer:function(val, meta, record) {
                return record.data.actor_name;
            }
        },
        {
            header:'Сколько',
            dataIndex:'amount',
            xtype:'numbercolumn',
            format:'0.00',
            summaryType:'sum',
            summaryRenderer: function(value, summaryData, dataIndex) {
                return Ext.util.Format.number(value, "0.00");
            },
            editor:{
                xtype:'numberfield',
                minValue:0.01,
                hideTrigger:true,
                keyNavEnabled:false,
                mouseWheelEnabled:false
            }
        },
        {
            header:'Категория',
            dataIndex:'category_name'
        },
        {
            header:'Комментарий',
            dataIndex:'comment',
            flex:1,
            autoScroll:true,
            renderer:function(val) {
                return "<span title='" + val + "'>" + val + "</span>";
            }
        }
    ],

    selType:'rowmodel',

    features:[
        {ftype:'summary'}
    ],

    viewConfig:{
        getRowClass:function (record) {
            var t = record.get('type');
            if (t.length == 1) return "acc-transrow-" + t;
            else return '';
        }
    },

    plugins: [
        {
            ptype:'rowediting',
            clicksToEdit: 2,
            listeners:{
                beforeedit:function(editor, vals) {
                    // Amount
                    var col = vals.grid.columns[2];
                    var field = col.getEditor();

                    if(vals.record.data.type == 'D') {
                        field.setMinValue(0.01);
                        field.setMaxValue(null);
                    } else {
                        field.setMinValue(null);
                        field.setMaxValue(-0.01);
                    }

//                    var oldField = null;
//                    if (col.cfield = ! null) oldField = col.cfield;
//
//                    col.cfield = Ext.create(col.custEditor);
//                    col.cfield.store = col.cEditorStore;
//                    col.setEditor(col.cfield);
//
//                    if (oldField != null)Ext.destroy(oldField);
                }
            }
        }
    ],

    proxyTrans:null,

    initComponent:function () {
        var me = this;

        me.proxyTrans = Ext.create('alexzam.his.model.account.proxy.Transaction', {
            rootUrl:me.rootUrl
        });

        me.store = Ext.create('alexzam.his.model.account.store.Transaction', {
            proxy:me.proxyTrans
        });

        me.callParent();

        me.getView().getFeature(0).disable();
    },

    reloadTrans:function(data) {
        var me = this;
        me.proxyTrans.extraParams = data;
        me.store.load();
    },

    enableSummary:function(enable) {
        var feat = this.getView().getFeature(0);
        if (enable) feat.enable();
        else feat.disable();
    }
});