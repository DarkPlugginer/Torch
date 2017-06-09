package net.minecraft.server;

import com.destroystokyo.paper.exception.ServerInternalException;
import com.google.common.collect.Lists;
import com.koloboke.collect.map.hash.HashObjObjMaps;
import com.koloboke.collect.map.hash.HashObjShortMaps;

import java.io.DataInputStream;
import java.io.DataOutput;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javax.annotation.Nullable;

public class PersistentCollection {

    private final IDataManager b;
    protected Map<String, PersistentBase> a = HashObjObjMaps.newMutableMap();
    public final List<PersistentBase> c = Lists.newArrayList(); // Spigot
    private final Map<String, Short> d = HashObjShortMaps.newMutableMap();

    public PersistentCollection(IDataManager idatamanager) {
        this.b = idatamanager;
        this.b();
    }

    @Nullable
    public PersistentBase get(Class<? extends PersistentBase> oclass, String s) {
        PersistentBase persistentbase = this.a.get(s);

        if (persistentbase != null) {
            return persistentbase;
        } else {
            if (this.b != null) {
                try {
                    File file = this.b.getDataFile(s);

                    if (file != null && file.exists()) {
                        try {
                            persistentbase = oclass.getConstructor(new Class[] { String.class}).newInstance(new Object[] { s});
                        } catch (Exception exception) {
                            throw new RuntimeException("Failed to instantiate " + oclass, exception);
                        }

                        FileInputStream fileinputstream = new FileInputStream(file);
                        NBTTagCompound nbttagcompound = NBTCompressedStreamTools.a(fileinputstream);

                        fileinputstream.close();
                        persistentbase.a(nbttagcompound.getCompound("data"));
                    }
                } catch (Exception exception1) {
                    exception1.printStackTrace();
                    ServerInternalException.reportInternalException(exception1); // Paper
                }
            }

            if (persistentbase != null) {
                this.a.put(s, persistentbase);
                this.c.add(persistentbase);
            }

            return persistentbase;
        }
    }

    public void a(String s, PersistentBase persistentbase) {
        if (this.a.containsKey(s)) {
            this.c.remove(this.a.remove(s));
        }

        this.a.put(s, persistentbase);
        this.c.add(persistentbase);
    }

    public void a() {
        for (int i = 0; i < this.c.size(); ++i) {
            PersistentBase persistentbase = this.c.get(i);

            if (persistentbase.d()) {
                this.a(persistentbase);
                persistentbase.a(false);
            }
        }

    }

    private void a(PersistentBase persistentbase) {
        if (this.b != null) {
            File file = this.b.getDataFile(persistentbase.id);

            if (file != null) {
                NBTTagCompound compound = new NBTTagCompound();
                compound.set("data", persistentbase.b(new NBTTagCompound()));
                
                // Torch start
                MCUtil.scheduleAsyncTask(() -> {
                    try {
                        FileOutputStream outputStream = new FileOutputStream(file);

                        NBTCompressedStreamTools.a(compound, outputStream);
                        outputStream.close();
                    } catch (Exception ex) {
                        ex.printStackTrace();
                        ServerInternalException.reportInternalException(ex); // Paper
                    }
                });
                // Torch end
            }
        }
    }

    private void b() {
        try {
            this.d.clear();
            if (this.b == null) {
                return;
            }

            File file = this.b.getDataFile("idcounts");

            if (file != null && file.exists()) {
                DataInputStream datainputstream = new DataInputStream(new FileInputStream(file));
                NBTTagCompound nbttagcompound = NBTCompressedStreamTools.a(datainputstream);

                datainputstream.close();
                Iterator iterator = nbttagcompound.c().iterator();

                while (iterator.hasNext()) {
                    String s = (String) iterator.next();
                    NBTBase nbtbase = nbttagcompound.get(s);

                    if (nbtbase instanceof NBTTagShort) {
                        NBTTagShort nbttagshort = (NBTTagShort) nbtbase;
                        short short0 = nbttagshort.f();

                        this.d.put(s, Short.valueOf(short0));
                    }
                }
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }

    }

    public int a(String s) {
        Short oshort = this.d.get(s);

        if (oshort == null) {
            oshort = Short.valueOf((short) 0);
        } else {
            oshort = Short.valueOf((short) (oshort.shortValue() + 1));
        }

        this.d.put(s, oshort);
        if (this.b == null) {
            return oshort.shortValue();
        } else {
            try {
                File file = this.b.getDataFile("idcounts");

                if (file != null) {
                    NBTTagCompound nbttagcompound = new NBTTagCompound();
                    Iterator iterator = this.d.keySet().iterator();

                    while (iterator.hasNext()) {
                        String s1 = (String) iterator.next();

                        nbttagcompound.setShort(s1, this.d.get(s1).shortValue());
                    }

                    DataOutputStream dataoutputstream = new DataOutputStream(new FileOutputStream(file));

                    NBTCompressedStreamTools.a(nbttagcompound, (DataOutput) dataoutputstream);
                    dataoutputstream.close();
                }
            } catch (Exception exception) {
                exception.printStackTrace();
            }

            return oshort.shortValue();
        }
    }
}
