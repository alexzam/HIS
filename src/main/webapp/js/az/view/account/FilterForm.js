Ext.define('alexzam.his.view.account.FilterForm', {
    extend:'Ext.form.Panel',
    alias:'widget.his.account.FilterForm',

    title:'Фильтр',
    layout:{
        type:'vbox',
        align:'stretch',
        constrainAlign:true
    },
    bodyPadding:5,
    autoScroll:true,
    items:[
        {
            xtype:'datefield',
            fieldLabel:'С',
            name:'from',
            format:'d.m.Y',
            validateOnChange:false,
            labelWidth:65,
            itemId:'dtFrom',
            startDay:1
        },
        {
            xtype:'datefield',
            fieldLabel:'По',
            name:'to',
            format:'d.m.Y',
            validateOnChange:false,
            labelWidth:65,
            itemId:'dtTo',
            startDay:1
        },
        {
            xtype:'combo',
            fieldLabel:'Категория',
            name:'cat',
            queryMode:'local',
            valueField:'id',
            displayField:'name',
            lastQuery:'',
            labelWidth:65,
            itemId:'cmbCat',
            multiSelect:true
        },
        {
            xtype:'button',
            text:'Удалить',
            handler:function() {
                this.ownerCt.fireEvent('transdelete');
            }
        }
    ],

    dtFrom:null,
    dtTo:null,
    cmbCat:null,
    storeCat:null,

    initComponent:function() {
        var me = this;
        me.storeCat = Ext.create('alexzam.his.model.account.store.Category', {
            proxy:Ext.create('alexzam.his.model.account.proxy.Category', {
                rootUrl:me.rootUrl
            })
        });

        me.items[2].store = me.storeCat; // Not so good but seems most efficient

        me.callParent();

        me.dtFrom = me.getComponent('dtFrom');
        me.dtTo = me.getComponent('dtTo');
        me.cmbCat = me.getComponent('cmbCat');

        me.dtFrom.on('change', me.onFilterChange, me);
        me.dtTo.on('change', me.onFilterChange, me);
        me.cmbCat.on('change', me.onFilterChange, me);

        me.addEvents(['filterupdate', 'transdelete']);

        var d = new Date();
        d.setDate(1);
        me.dtFrom.setValue(d);
    },

    reloadCategories:function() {
        var me = this;
        var cmp = me.cmbCat;
        var val = cmp.getValue();
        cmp.setValue('');
        me.storeCat.load({
            callback:function() {
                var found = me.storeCat.find('id', val);
                if (found >= 0) cmp.setValue(val);
                cmp.getPicker().setLoading(false);
            }
        });
    },

    onFilterChange:function() {
        var me = this;
        me.fireEvent('filterupdate');

        me.dtTo.setMinValue(me.dtFrom.getValue());
        me.dtFrom.setMaxValue(me.dtTo.getValue());
    },

    getValues:function(asString){
        var frm = this.getForm();

        if (!frm.isValid()) return null;
        // TODO Review. Maybe there is a way to set converter to individual fields?
        var q = frm.getFieldValues();

        if (q.from != null) {
            q.from = q.from.getTime();
        }
        if (q.to != null) {
            q.to = q.to.getTime();
        }

        if(asString != true) return q;

        var ret = [];
        for (key in q){
            if(q[key] == null) continue;
            ret.push(key + "=" + q[key])
        }
        return ret.join("&");
    }
});