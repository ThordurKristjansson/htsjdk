package htsjdk.samtools.cram.encoding.rans;

import java.nio.ByteBuffer;

class D04 {
    static void uncompress(ByteBuffer in, Decoding.ari_decoder D,
                           Decoding.RansDecSymbol[] syms, ByteBuffer out) {
        int rans0, rans1, rans2, rans3;
        rans0 = in.getInt();
        rans1 = in.getInt();
        rans2 = in.getInt();
        rans3 = in.getInt();

        int out_sz = out.remaining();
        int out_end = (out_sz & ~3);
        for (int i = 0; i < out_end; i += 4) {
            byte c0 = D.R[Decoding.RansDecGet(rans0, Constants.TF_SHIFT)];
            byte c1 = D.R[Decoding.RansDecGet(rans1, Constants.TF_SHIFT)];
            byte c2 = D.R[Decoding.RansDecGet(rans2, Constants.TF_SHIFT)];
            byte c3 = D.R[Decoding.RansDecGet(rans3, Constants.TF_SHIFT)];

            out.put(i + 0, c0);
            out.put(i + 1, c1);
            out.put(i + 2, c2);
            out.put(i + 3, c3);

            rans0 = Decoding.RansDecAdvanceSymbolStep(rans0, syms[0xFF & c0],
                    Constants.TF_SHIFT);
            rans1 = Decoding.RansDecAdvanceSymbolStep(rans1, syms[0xFF & c1],
                    Constants.TF_SHIFT);
            rans2 = Decoding.RansDecAdvanceSymbolStep(rans2, syms[0xFF & c2],
                    Constants.TF_SHIFT);
            rans3 = Decoding.RansDecAdvanceSymbolStep(rans3, syms[0xFF & c3],
                    Constants.TF_SHIFT);

            rans0 = Decoding.RansDecRenorm(rans0, in);
            rans1 = Decoding.RansDecRenorm(rans1, in);
            rans2 = Decoding.RansDecRenorm(rans2, in);
            rans3 = Decoding.RansDecRenorm(rans3, in);
        }

        out.position(out_end);
        byte c;
        switch (out_sz & 3) {
            case 0:
                break;
            case 1:
                c = D.R[Decoding.RansDecGet(rans0, Constants.TF_SHIFT)];
                Decoding.RansDecAdvanceSymbol(rans0, in, syms[0xFF & c],
                        Constants.TF_SHIFT);
                out.put(c);
                break;

            case 2:
                c = D.R[Decoding.RansDecGet(rans0, Constants.TF_SHIFT)];
                Decoding.RansDecAdvanceSymbol(rans0, in, syms[0xFF & c],
                        Constants.TF_SHIFT);
                out.put(c);

                c = D.R[Decoding.RansDecGet(rans1, Constants.TF_SHIFT)];
                Decoding.RansDecAdvanceSymbol(rans1, in, syms[0xFF & c],
                        Constants.TF_SHIFT);
                out.put(c);
                break;

            case 3:
                c = D.R[Decoding.RansDecGet(rans0, Constants.TF_SHIFT)];
                Decoding.RansDecAdvanceSymbol(rans0, in, syms[0xFF & c],
                        Constants.TF_SHIFT);
                out.put(c);

                c = D.R[Decoding.RansDecGet(rans1, Constants.TF_SHIFT)];
                Decoding.RansDecAdvanceSymbol(rans1, in, syms[0xFF & c],
                        Constants.TF_SHIFT);
                out.put(c);

                c = D.R[Decoding.RansDecGet(rans2, Constants.TF_SHIFT)];
                Decoding.RansDecAdvanceSymbol(rans2, in, syms[0xFF & c],
                        Constants.TF_SHIFT);
                out.put(c);
                break;
        }

        out.position(0);
    }
}
